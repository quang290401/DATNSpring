package com.example.datn.controller;
import com.example.datn.dto.HoaDonCTDTO;
import com.example.datn.dto.ProductDetailDTO;
import com.example.datn.dto.TrangThaiHoaDonDTO;
import com.example.datn.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.datn.Repository.*;
import com.example.datn.entity.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@Controller

public class HoaDonController {
    @GetMapping("/hoa-don")
    public String home(Model model, HttpSession session){
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "web/hoaDon";
    }
    private static final Logger logger = LoggerFactory.getLogger(HoaDonController.class);

    private final HoaDonRepository hoaDonRepository;
    final UserRepository userRepository;
    final UsersRepository usersRepository;

    private final ChiTietSPRepository chiTietSPRepository;
    private final HoaDonCTRepository hoaDonCTRepository;
    private final TrangThaiHDRepository trangThaiHDRepository;

    public HoaDonController(HoaDonRepository hoaDonRepository, UsersRepository usersRepository ,UserRepository userRepository, ChiTietSPRepository chiTietSPRepository, HoaDonCTRepository hoaDonCTRepository, TrangThaiHDRepository trangThaiHDRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.userRepository = userRepository;
        this.usersRepository = usersRepository;
        this.chiTietSPRepository = chiTietSPRepository;
        this.hoaDonCTRepository = hoaDonCTRepository;

        this.trangThaiHDRepository = trangThaiHDRepository;
    }

    @GetMapping("/getAll")
    public String getAllHoaDon(Model model) {
        List<HoaDonEntity> danhSachHoaDon = hoaDonRepository.findAll();
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);

        // Lọc các hóa đơn đã thanh toán
        List<HoaDonEntity> danhSachHoaDonDaThanhToan = danhSachHoaDon.stream()
                .filter(hd -> hd.getTrangThaiHD() != null && hd.getTrangThaiHD().getTrangThai().equals("1"))
                .collect(Collectors.toList());
        model.addAttribute("danhSachHoaDonDaThanhToan", danhSachHoaDonDaThanhToan);

        List<HoaDonEntity> danhSachHoaDonChuaThanhToan = danhSachHoaDon.stream()
                .filter(hd -> hd.getTrangThaiHD() != null && hd.getTrangThaiHD().getTrangThai().equals("0"))
                .collect(Collectors.toList());
        model.addAttribute("danhSachHoaDonChuaThanhToan", danhSachHoaDonChuaThanhToan);
        List<HoaDonEntity> danhSachHoaDonCho = hoaDonRepository.findHoaDonsByTrangThai();
        model.addAttribute("danhSachHoaDonCho", danhSachHoaDonCho);
        List<HoaDonEntity> hoaDonHuy = hoaDonRepository.findTTByHoaDon();
        model.addAttribute("hoaDonHuy", hoaDonHuy);
        List<HoaDonEntity> hdDangGiao = hoaDonRepository.findDangGiao();
        model.addAttribute("hdDangGiao", hdDangGiao);
        return "admin/adminWeb/HoaDon";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("hoaDon", new HoaDonEntity());
        return "admin/adminWeb/HoaDon";
    }

@PostMapping("/create")
public String createHoaDon(@ModelAttribute("hoaDon") HoaDonEntity hoaDon, RedirectAttributes redirectAttributes) {
    try {
        hoaDon.setCreateDate(LocalDate.now());
        if (hoaDon.getTongTien() == null) {
            hoaDon.setTongTien(BigDecimal.ZERO);
        }
        TrangThaiHDEntity trangThaiChuaThanhToan = trangThaiHDRepository.findByTrangThai("0");
        if (trangThaiChuaThanhToan != null) {
            hoaDon.setTrangThaiHD(trangThaiChuaThanhToan);
        } else {
            redirectAttributes.addFlashAttribute("error", "Trạng thái hóa đơn không hợp lệ.");
            return "redirect:/getAll";
        }

        hoaDonRepository.save(hoaDon);
        redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
    }
    return "redirect:/getAll";
}
    @GetMapping("/hoadon/detail/{id}")
    public String getDiaChiDetail(@PathVariable("id") UUID id, Model model) {
        Optional<HoaDonEntity> hoaDonOptional = hoaDonRepository.findById(id);
        HoaDonEntity hoaDonEntity = hoaDonRepository.findById(id).orElse(null);
        String trangThaiHienTai = hoaDonEntity.getTrangThaiHD().getTrangThai();
        System.out.println("Trang Thai hien tai1: " + trangThaiHienTai);
        if (hoaDonOptional.isPresent()) {
            HoaDonEntity hoaDon = hoaDonOptional.get();
            model.addAttribute("hoaDon", hoaDon);
            return "admin/adminWeb/HoaDonDetail";
        } else {
            model.addAttribute("errorMessage", "Hóa đơn không tồn tại.");
            return "admin/adminWeb/HoaDonDetail";
        }

    }


    @PostMapping("/hoadon/update/{id}")
    public String updateHoaDon(@PathVariable("id") UUID id,
                               @RequestParam("trangThai") String trangThai,
                               RedirectAttributes redirectAttributes) {
        try {
            // Check if the invoice exists
            if (!hoaDonRepository.existsById(id)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Hóa đơn không tồn tại.");
                return "redirect:/hoadon/detail/" + id;
            }

            // Fetch the invoice entity
            HoaDonEntity hoaDonEntity = hoaDonRepository.findById(id).orElse(null);
            if (hoaDonEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Hóa đơn không tồn tại.");
                return "redirect:/getAll";
            }
            String trangThaiHienTai = hoaDonEntity.getTrangThaiHD().getTrangThai();
            System.out.println("Trang Thai hien tai: " + trangThaiHienTai);
            if ("4".equals(trangThai)) {
                for (HoaDonChiTietEntity chiTiet : hoaDonEntity.getHoaDonChiTiets()) {
                    SanPhamChiTietEntity sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
                    int soLuong = chiTiet.getSoLuong();
                    System.out.println("Old So Luong: " + sanPhamChiTiet.getSoLuong());
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + soLuong);
                    System.out.println("New So Luong: " + sanPhamChiTiet.getSoLuong());
                    chiTietSPRepository.save(sanPhamChiTiet);
                }
            }

            // Update the invoice status
            TrangThaiHDEntity trangThaiHDEntity = trangThaiHDRepository.findByTrangThai(trangThai);
            if (trangThaiHDEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Trạng thái không hợp lệ.");
                return "redirect:/hoadon/detail/" + id;
            }
            hoaDonEntity.setTrangThaiHD(trangThaiHDEntity);
            hoaDonRepository.save(hoaDonEntity);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái hóa đơn thành công.");
            return "redirect:/getAll";

        } catch (Exception e) {
            // Add error message and redirect in case of an exception
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật hóa đơn.");
            return "redirect:/hoadon/detail/" + id; // Redirect to the detail page with the error message
        }
    }



    @GetMapping("/addToCart/{hoaDonId}")
    public String showAddToCartForm(@PathVariable UUID hoaDonId, Model model) {
        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
        List<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.findAll();
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        if (!optionalHoaDon.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId);
        }
        List<HoaDonEntity> danhSachHoaDon = hoaDonRepository.findAll();
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);
        return "admin/adminWeb/BanHangOff";
    }

    @PostMapping("/updateDetail")
    public ResponseEntity<String> updateDetail(@RequestParam UUID hoaDonId, @RequestParam UUID sanPhamChiTietId, @RequestParam int soLuong) {
        // Create an instance of HoaDonChiTietId
        HoaDonChiTietId id = new HoaDonChiTietId(hoaDonId, sanPhamChiTietId);
        // Find the detail by id
        Optional<HoaDonChiTietEntity> optionalChiTiet = hoaDonCTRepository.findById(id);
        if (optionalChiTiet.isPresent()) {
            // Update the quantity
            HoaDonChiTietEntity chiTiet = optionalChiTiet.get();
            chiTiet.setSoLuong(soLuong);
            hoaDonCTRepository.save(chiTiet);
            return ResponseEntity.ok("Số lượng đã được cập nhật.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chi tiết hóa đơn không tồn tại.");
        }
    }


    @PostMapping("/deleteDetail/{hoaDonId}/{sanPhamChiTietId}")
    public ResponseEntity<String> deleteDetail(@PathVariable UUID hoaDonId, @PathVariable UUID sanPhamChiTietId) {
        // Create an instance of HoaDonChiTietId
        HoaDonChiTietId id = new HoaDonChiTietId(hoaDonId, sanPhamChiTietId);
        boolean exists = hoaDonCTRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chi tiết hóa đơn không tồn tại.");
        }

        // Delete the detail
        hoaDonCTRepository.deleteById(id);
        return ResponseEntity.ok("Chi tiết hóa đơn đã được xóa.");
    }


    @GetMapping("/trangThai/{hoaDonId}")
    public ResponseEntity<TrangThaiHoaDonDTO> getInvoiceStatus(@PathVariable UUID hoaDonId) {
        TrangThaiHoaDonDTO trangThaiDTO = hoaDonRepository.findTrangThaiByHoaDonId(hoaDonId);
        if (trangThaiDTO != null) {
            return ResponseEntity.ok(trangThaiDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sanphamCT/{sanPhamId}")
    public ResponseEntity<Map<String, Object>> layThongTinSanPhamChiTiet(@PathVariable("sanPhamId") UUID sanPhamChiTietId) {
        Map<String, Object> thongTinSanPham = chiTietSPRepository.findByNameSP(sanPhamChiTietId);
        if (!thongTinSanPham.isEmpty()) {
            return ResponseEntity.ok(thongTinSanPham);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getDetails/{hoaDonId}")
    public ResponseEntity<String> getDetails(@PathVariable("hoaDonId") String id) {
        UUID hoaDonId;
        try {
            hoaDonId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Định dạng UUID không hợp lệ");
        }

        List<HoaDonChiTietEntity> chiTietEntities = hoaDonCTRepository.findByHoaDon_Id(hoaDonId);

        if (chiTietEntities == null || chiTietEntities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<HoaDonCTDTO> chiTietDTOs = chiTietEntities.stream()
                .map(entity -> new HoaDonCTDTO(
                        entity.getSanPhamChiTiet().getId(),
                        entity.getSanPhamChiTiet().getSanPham().getTenSanPham(),
                        entity.getSanPhamChiTiet().getMauSac() != null ? entity.getSanPhamChiTiet().getMauSac().getTen() : "N/A",
                        entity.getSanPhamChiTiet().getKichCo() != null ? entity.getSanPhamChiTiet().getKichCo().getTenKichCo() : "N/A",
                        entity.getSoLuong(),
                        entity.getSanPhamChiTiet().getGiaSanPham(),
                        entity.getThanhTien()
                ))
                .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(chiTietDTOs);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // In ra thông tin chi tiết lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi chuyển đổi sang JSON");
        }
    }
    @GetMapping("/getProductDetail/{hoaDonId}/{sanPhamChiTietId}")
    public ResponseEntity<String> getProductDetail(
            @PathVariable("hoaDonId") String hoaDonIdStr,
            @PathVariable("sanPhamChiTietId") String sanPhamChiTietIdStr) {

        UUID hoaDonId;
        UUID sanPhamChiTietId;

        try {
            hoaDonId = UUID.fromString(hoaDonIdStr);
            sanPhamChiTietId = UUID.fromString(sanPhamChiTietIdStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Định dạng UUID không hợp lệ");
        }

        // Tìm chi tiết hóa đơn theo ID
        HoaDonChiTietId primaryKey = new HoaDonChiTietId(hoaDonId, sanPhamChiTietId);
        Optional<HoaDonChiTietEntity> chiTietOptional = hoaDonCTRepository.findById(primaryKey);

        if (!chiTietOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        HoaDonChiTietEntity chiTiet = chiTietOptional.get();
        SanPhamChiTietEntity sanPhamChiTiet = chiTiet.getSanPhamChiTiet();

        ProductDetailDTO productDetailDTO = new ProductDetailDTO(
                sanPhamChiTiet.getId(),
                sanPhamChiTiet.getSanPham() != null ? sanPhamChiTiet.getSanPham().getTenSanPham() : "N/A",
                sanPhamChiTiet.getMauSac() != null ? sanPhamChiTiet.getMauSac().getTen() : "N/A",
                sanPhamChiTiet.getKichCo() != null ? sanPhamChiTiet.getKichCo().getTenKichCo() : "N/A",
                chiTiet.getSoLuong(),
                sanPhamChiTiet.getGiaSanPham(),
                chiTiet.getThanhTien()
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(productDetailDTO);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // In ra thông tin chi tiết lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi chuyển đổi sang JSON");
        }
    }

@PostMapping("/addToCart/{hoaDonId}")
public String addProductToHoaDon(@PathVariable(required = false) String hoaDonId,
                                 @RequestParam UUID sanPhamChiTietId,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {
    try {
        if (hoaDonId == null || hoaDonId.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có ID hóa đơn. Vui lòng chọn hóa đơn trước.");
            return "redirect:/getAll";
        }
        UUID hoaDonUUID;
        try {
            hoaDonUUID = UUID.fromString(hoaDonId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", " Vui lòng chọn hóa đơn trước.f");
            return "redirect:/getAll";
        }

        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonUUID);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonUUID);
            return "redirect:/getAll";
        }
        HoaDonEntity hoaDon = optionalHoaDon.get();

        Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
        if (!optionalSanPhamChiTiet.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
            return "redirect:/addToCart/" + hoaDonUUID;
        }
        SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();

        if (quantity <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Số lượng sản phẩm phải là số dương");
            return "redirect:/addToCart/" + hoaDonUUID;
        }

        // Lấy hoặc tạo mới HoaDonChiTietEntity
        HoaDonChiTietId primaryKey = new HoaDonChiTietId(hoaDonUUID, sanPhamChiTietId);
        Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);
        int existingQuantity = 0;
        if (optionalHoaDonChiTiet.isPresent()) {
            existingQuantity = optionalHoaDonChiTiet.get().getSoLuong();
        }

        // Kiểm tra nếu số lượng yêu cầu vượt quá số lượng còn lại trong kho
        if (quantity > sanPhamChiTiet.getSoLuong()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
            return "redirect:/addToCart/" + hoaDonUUID;
        }
        // Cập nhật hoặc tạo mới HoaDonChiTietEntity
        HoaDonChiTietEntity hoaDonChiTiet;
        if (optionalHoaDonChiTiet.isPresent()) {
            // Cập nhật bản ghi đã tồn tại
            hoaDonChiTiet = optionalHoaDonChiTiet.get();
            hoaDonChiTiet.setSoLuong(existingQuantity + quantity);
            hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(existingQuantity + quantity)));
        } else {
            // Tạo mới bản ghi
            hoaDonChiTiet = new HoaDonChiTietEntity();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(quantity);
            hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
            hoaDonChiTiet.setCreateDate(LocalDate.now());
        }
        // Lưu hoặc cập nhật HoaDonChiTietEntity
        hoaDonCTRepository.save(hoaDonChiTiet);

        // Giảm số lượng sản phẩm trong kho (chỉ sau khi đã lưu vào HoaDonChiTiet)
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
        chiTietSPRepository.save(sanPhamChiTiet);

        // Cập nhật tổng tiền trong HoaDonEntity
        BigDecimal tongTien = hoaDon.getTongTien().add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
        hoaDon.setTongTien(tongTien);
        hoaDonRepository.save(hoaDon);

        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào hóa đơn thành công");
        return "redirect:/addToCart/" + hoaDonUUID;
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        return "redirect:/addToCart/" + hoaDonId;
    }
}
    @PostMapping("/delete/{id}")
    public String deleteHoaDon(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(id);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Hóa đơn này không tồn tại");
            return "redirect:/getAll";
        }
        HoaDonEntity hoaDon = optionalHoaDon.get();

        // Retrieve all HoaDonChiTietEntities for this HoaDon
        List<HoaDonChiTietEntity> hoaDonChiTietList = hoaDonCTRepository.findByHoaDonId(id);

        // Cộng lại số lượng sản phẩm cho tất cả các chi tiết hóa đơn
        for (HoaDonChiTietEntity chiTiet : hoaDonChiTietList) {
            SanPhamChiTietEntity sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + chiTiet.getSoLuong());
            chiTietSPRepository.save(sanPhamChiTiet);
        }
        hoaDonCTRepository.deleteAll(hoaDonChiTietList);
        hoaDonRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Hóa đơn đã được xóa thành công và số lượng sản phẩm đã được cập nhật.");
        return "redirect:/getAll";
    }
    @PostMapping("/sanphamct/delete/{hoaDonId}/{sanPhamCTid}")
    public ResponseEntity<Map<String, String>> deleteSanPhamCT(@PathVariable("hoaDonId") UUID hoaDonId,
                                                               @PathVariable("sanPhamCTid") UUID sanPhamCTid) {
        Map<String, String> response = new HashMap<>();
        try {
            // Check if the invoice exists
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (!optionalHoaDon.isPresent()) {
                response.put("message", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            HoaDonEntity hoaDon = optionalHoaDon.get();

            // Check if the product detail exists in the invoice
            HoaDonChiTietId primaryKey = new HoaDonChiTietId(hoaDonId, sanPhamCTid);
            Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);
            if (!optionalHoaDonChiTiet.isPresent()) {
                response.put("message", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamCTid + " trong hóa đơn này");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            HoaDonChiTietEntity hoaDonChiTiet = optionalHoaDonChiTiet.get();
            // Update the product quantity in stock
            SanPhamChiTietEntity sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
            chiTietSPRepository.save(sanPhamChiTiet);
            // Delete the product detail from the invoice
            hoaDonCTRepository.delete(hoaDonChiTiet);
            // Update the total amount in the invoice
            BigDecimal tongTienMoi = hoaDon.getTongTien().subtract(hoaDonChiTiet.getThanhTien());
            hoaDon.setTongTien(tongTienMoi);
            hoaDonRepository.save(hoaDon);

            response.put("message", "Sản phẩm chi tiết đã được xóa thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Đã xảy ra lỗi, vui lòng thử lại");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/updateTrangThai/{hoaDonId}")
    public ResponseEntity<Map<String, String>> updateTrangThai(@PathVariable("hoaDonId") UUID hoaDonId) {
        Map<String, String> response = new HashMap<>();
        try {
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (!optionalHoaDon.isPresent()) {
                response.put("message", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            HoaDonEntity hoaDon = optionalHoaDon.get();
//            hoaDon.setTrangThai(hoaDon.getTrangThai() + 1);
            hoaDonRepository.save(hoaDon);

            response.put("message", "Trạng thái hóa đơn đã được cập nhật thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Đã xảy ra lỗi, vui lòng thử lại");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/payment/{hoaDonId}")
    public String showPaymentForm(@PathVariable("hoaDonId") UUID hoaDonId, Model model, RedirectAttributes redirectAttributes) {
        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
        if (optionalHoaDon.isEmpty()) {
            return "redirect:/getAll";
        }
        HoaDonEntity hoaDon = optionalHoaDon.get();
        List<HoaDonChiTietEntity> chiTietHoaDon = hoaDon.getHoaDonChiTiets();

        // Kiểm tra nếu hóa đơn không có sản phẩm
        if (chiTietHoaDon.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hóa đơn không có sản phẩm để thanh toán");
            return "redirect:/getAll";
        }
        model.addAttribute("hoaDon", hoaDon);
        return "admin/adminWeb/ThanhToan";
    }
//    @PostMapping("/payment/{hoaDonId}")
//    public String processPayment(
//            @PathVariable("hoaDonId") UUID hoaDonId,
//            @RequestParam(required = false) String tenKhachHang,
//            @RequestParam(required = false) String sdt,
//            RedirectAttributes redirectAttributes) {
//        try {
//            // Tìm kiếm HoaDonEntity theo ID
//            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//            if (optionalHoaDon.isEmpty()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                return "redirect:/getAll";
//            }
//            HoaDonEntity hoaDon = optionalHoaDon.get();
//            // Tìm kiếm TrangThaiHDEntity theo tên "Đã Thanh Toán"
//            Optional<TrangThaiHDEntity> daThanhToanOpt = Optional.ofNullable(trangThaiHDRepository.findByTrangThai("1"));
//            if (daThanhToanOpt.isEmpty()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Trạng thái 'Đã Thanh Toán' không tìm thấy.");
//                return "redirect:/getAll";
//            }
//            TrangThaiHDEntity daThanhToan = daThanhToanOpt.get();
//            hoaDon.setTrangThaiHD(daThanhToan);
//            hoaDon.setNgayThanhToan(LocalDate.now());
//
//            UserEntity userEntity = null;
//            if (tenKhachHang != null && !tenKhachHang.trim().isEmpty() && sdt != null && !sdt.trim().isEmpty()) {
//                List<UserEntity> userEntities = userRepository.findByTenAndSdt(tenKhachHang, sdt);
//                if (!userEntities.isEmpty()) {
//                    userEntity = userEntities.get(0);
//                }
//                // Nếu không tìm thấy khách hàng, tạo mới
//                if (userEntity == null) {
//                    userEntity = new UserEntity();
//                    userEntity.setTaiKhoan("");
//                    userEntity.setNgaySinh(LocalDate.parse("2024-01-01"));
//                    userEntity.setHo("/");
//                    userEntity.setTenDem("/");
//                    userEntity.setTen(tenKhachHang);
//                    userEntity.setNgaySinh(LocalDate.now());
//                    userEntity.setSdt(sdt);
//                    userEntity.setGioiTinh(1);
//                    userEntity.setTrangThai(1);
//                    userEntity = userRepository.save(userEntity);
//                }
//            } else {
//                userEntity = userRepository.findByTen("Khách lẻ").orElseGet(() -> {
//                    UserEntity defaultUser = new UserEntity();
//                    defaultUser.setTen("Khách lẻ");
//                    defaultUser.setTrangThai(0); // Thiết lập các thuộc tính khác nếu cần
//                    return userRepository.save(defaultUser);
//                });
//            }
//            hoaDon.setUser(userEntity);
//            hoaDonRepository.save(hoaDon);
//            redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn thành công");
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Chuỗi UUID không hợp lệ cho hoaDonId");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại: " + e.getMessage());
//        }
//        return "redirect:/getAll";
//    }
@PostMapping("/payment/{hoaDonId}")
public String processPayment(
        @PathVariable("hoaDonId") UUID hoaDonId,
        @RequestParam(required = false) String tenKhachHang,
        @RequestParam(required = false) String sdt,
        RedirectAttributes redirectAttributes) {
    try {
        // Tìm kiếm HoaDonEntity theo ID
        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
        if (optionalHoaDon.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
            return "redirect:/getAll";
        }
        HoaDonEntity hoaDon = optionalHoaDon.get();

        // Tìm kiếm TrangThaiHDEntity theo tên "Đã Thanh Toán"
        Optional<TrangThaiHDEntity> daThanhToanOpt = Optional.ofNullable(trangThaiHDRepository.findByTrangThai("1"));
        if (daThanhToanOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Trạng thái 'Đã Thanh Toán' không tìm thấy.");
            return "redirect:/getAll";
        }
        TrangThaiHDEntity daThanhToan = daThanhToanOpt.get();
        hoaDon.setTrangThaiHD(daThanhToan);
        hoaDon.setNgayThanhToan(LocalDate.now());
        UserEntity userEntity;
        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            userEntity = userRepository.findByTen("Khách lẻ").orElseGet(() -> {
                UserEntity defaultUser = new UserEntity();
                defaultUser.setTen("Khách lẻ");
                defaultUser.setTrangThai(0);
                return userRepository.save(defaultUser);
            });
        } else {
            List<UserEntity> userEntities = userRepository.findByTenAndSdt(tenKhachHang, sdt);
            if (userEntities.isEmpty()) {
                userEntity = new UserEntity();
                userEntity.setTaiKhoan("");
                userEntity.setNgaySinh(LocalDate.parse("2024-01-01")); // Thiết lập ngày sinh mặc định
                userEntity.setHo("/");
                userEntity.setTenDem("/");
                userEntity.setTen(tenKhachHang);
                userEntity.setSdt(sdt);
                userEntity.setGioiTinh(1);
                userEntity.setTrangThai(1);
                userEntity = userRepository.save(userEntity);
            } else if (userEntities.size() == 1) {
                userEntity = userEntities.get(0);
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Có nhiều khách hàng với cùng tên và số điện thoại.");
                return "redirect:/getAll";
            }
        }
        hoaDon.setUser(userEntity);
        hoaDonRepository.save(hoaDon);

        redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn thành công");
    } catch (IllegalArgumentException e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Chuỗi UUID không hợp lệ cho hoaDonId");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại: " + e.getMessage());
    }
    return "redirect:/getAll";
}

}