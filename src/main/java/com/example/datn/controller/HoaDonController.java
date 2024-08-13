package com.example.datn.controller;
import com.example.datn.dto.HoaDonCTDTO;
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
    private final ChiTietSPRepository chiTietSPRepository;
    private final HoaDonCTRepository hoaDonCTRepository;
    private final TrangThaiHDRepository trangThaiHDRepository;

    public HoaDonController(HoaDonRepository hoaDonRepository, UserRepository userRepository, ChiTietSPRepository chiTietSPRepository, HoaDonCTRepository hoaDonCTRepository, TrangThaiHDRepository trangThaiHDRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.userRepository = userRepository;
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
        return "admin/adminWeb/HoaDon";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("hoaDon", new HoaDonEntity());
        return "admin/adminWeb/HoaDon";
    }

//    @PostMapping("/create")
//    public String createHoaDon(@ModelAttribute("hoaDon") HoaDonEntity hoaDon, RedirectAttributes redirectAttributes) {
//        try {
//            Optional<TrangThaiHDEntity> chuaThanhToanOpt = trangThaiHDRepository.findByTen("Chưa Thanh Toán");
//            if (chuaThanhToanOpt.isEmpty()) {
//                redirectAttributes.addFlashAttribute("error", "Trạng thái chưa thanh toán không tìm thấy.");
//                return "redirect:/getAll";
//            }
//            TrangThaiHDEntity chuaThanhToan = chuaThanhToanOpt.get();
//            hoaDon.setTrangThaiHD(chuaThanhToan);
//            hoaDon.setCreateDate(LocalDate.now());
//            if (hoaDon.getTongTien() == null) {
//                hoaDon.setTongTien(BigDecimal.ZERO);
//            }
//            hoaDonRepository.save(hoaDon);
//
//            // Thông báo thành công
//            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công.");
//
//        } catch (Exception e) {
//            // Xử lý lỗi nếu có
//            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
//        }
//
//        // Chuyển hướng đến danh sách hóa đơn
//        return "redirect:/getAll";
//    }
@PostMapping("/create")
public String createHoaDon(@ModelAttribute("hoaDon") HoaDonEntity hoaDon, RedirectAttributes redirectAttributes) {
    try {
        hoaDon.setCreateDate(LocalDate.now());
        if (hoaDon.getTongTien() == null) {
            hoaDon.setTongTien(BigDecimal.ZERO);
        }

        // Lấy trạng thái "Chưa Thanh Toán" từ cơ sở dữ liệu
        TrangThaiHDEntity trangThaiChuaThanhToan = trangThaiHDRepository.findByTrangThai("0");
        if (trangThaiChuaThanhToan != null) {
            hoaDon.setTrangThaiHD(trangThaiChuaThanhToan);
        } else {
            // Xử lý lỗi nếu trạng thái không tìm thấy
            redirectAttributes.addFlashAttribute("error", "Trạng thái hóa đơn không hợp lệ.");
            return "redirect:/getAll";
        }

        hoaDonRepository.save(hoaDon);
        redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công.");

    } catch (Exception e) {
        // Xử lý lỗi nếu có
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
    }

    // Chuyển hướng đến danh sách hóa đơn
    return "redirect:/getAll";
}
    @GetMapping("/hoadon/detail/{id}")
    public String getDiaChiDetail(@PathVariable("id") UUID id, Model model) {
        Optional<HoaDonEntity> hoaDonOptional = hoaDonRepository.findById(id);
        if (hoaDonOptional.isPresent()) {
            HoaDonEntity hoaDon = hoaDonOptional.get();
            model.addAttribute("hoaDon", hoaDon);
            // Không cần thêm gì nữa vì trạng thái đã được chứa trong đối tượng hoaDon
            return "admin/adminWeb/HoaDonDetail";
        } else {
            model.addAttribute("errorMessage", "Hóa đơn không tồn tại.");
            return "admin/adminWeb/HoaDonDetail";
        }
    }

    @PostMapping("/hoadon/update/{id}")
    public String updateHoaDon(@PathVariable("id") UUID id,
                               @RequestParam("trangThai") String trangThai,
                               Model model) {
        try {
            if (!hoaDonRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Hóa đơn không tồn tại.");
                List<HoaDonEntity> hoaDonList = hoaDonRepository.findAll();
                model.addAttribute("hoaDonList", hoaDonList);
                return "admin/adminWeb/HoaDon";
            }

            // Lấy thông tin hóa đơn cần cập nhật từ cơ sở dữ liệu
            HoaDonEntity hoaDonEntity = hoaDonRepository.findById(id).orElse(null);
            if (hoaDonEntity == null) {
                return "redirect:/getAll";
            }
            // Lấy đối tượng TrangThaiHDEntity dựa trên trạng thái
            TrangThaiHDEntity trangThaiHDEntity = trangThaiHDRepository.findByTrangThai(trangThai);
            if (trangThaiHDEntity == null) {
                model.addAttribute("errorMessage", "Trạng thái không hợp lệ.");
                return "redirect:/hoadon/detail";
            }
            // Cập nhật trạng thái hóa đơn
            hoaDonEntity.setTrangThaiHD(trangThaiHDEntity);
            hoaDonRepository.save(hoaDonEntity); // Lưu thay đổi vào cơ sở dữ liệu

            return "redirect:/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật hóa đơn.");
            return "redirect:/hoadon/detail";
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

    //    @PostMapping("/addToCart/{hoaDonId}")
//    public String addProductToHoaDon(@PathVariable UUID hoaDonId,
//                                     @RequestParam UUID sanPhamChiTietId,
//                                     @RequestParam int quantity,
//                                     RedirectAttributes redirectAttributes) {
//        try {
//            // Retrieve HoaDonEntity using hoaDonId
//            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//            if (!optionalHoaDon.isPresent()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                return "redirect:/hoaDon/getAll";
//            }
//            HoaDonEntity hoaDon = optionalHoaDon.get();
//
//            // Retrieve SanPhamChiTietEntity using sanPhamChiTietId
//            Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
//            if (!optionalSanPhamChiTiet.isPresent()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
//                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//            SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();
//
//            // Validate quantity
//            if (quantity <= 0) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Số lượng sản phẩm phải là số dương");
//                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//
//            // Check if enough quantity is available
//            if (quantity > sanPhamChiTiet.getSoLuong()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
//                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//
//            // Retrieve HoaDonChiTietEntity if it exists
//            HoaDonChiTietPK primaryKey = new HoaDonChiTietPK();
//            primaryKey.setHoaDonId(hoaDonId);
//            primaryKey.setSanPhamChiTietId(sanPhamChiTietId);
//            Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);
//
//            HoaDonChiTietEntity hoaDonChiTiet;
//            if (optionalHoaDonChiTiet.isPresent()) {
//                // Update existing HoaDonChiTietEntity
//                hoaDonChiTiet = optionalHoaDonChiTiet.get();
//                int newQuantity = hoaDonChiTiet.getSoLuong() + quantity;
//                if (newQuantity > sanPhamChiTiet.getSoLuong()) {
//                    redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để cập nhật hóa đơn");
//                    return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//                }
//                hoaDonChiTiet.setSoLuong(newQuantity);
//                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(newQuantity)));
//            } else {
//                // Create new HoaDonChiTietEntity instance
//                hoaDonChiTiet = new HoaDonChiTietEntity();
//                hoaDonChiTiet.setId(primaryKey);
//                hoaDonChiTiet.setHoaDon(hoaDon);
//                hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
//                hoaDonChiTiet.setSoLuong(quantity);
//                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
//                hoaDonChiTiet.setCreateDate(LocalDate.now());
//            }
//
//            // Save or update HoaDonChiTietEntity
//            hoaDonCTRepository.save(hoaDonChiTiet);
//
//            // Update total amount in HoaDonEntity
//            BigDecimal tongTien = hoaDon.getTongTien().add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
//            hoaDon.setTongTien(tongTien);
//            hoaDonRepository.save(hoaDon);
//
//            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào hóa đơn thành công");
//            return "redirect:/hoaDon/getAll";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
//            return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//        }
//    }
    @PostMapping("/addToCart/{hoaDonId}")
    public String addProductToHoaDon(@PathVariable UUID hoaDonId,
                                     @RequestParam UUID sanPhamChiTietId,
                                     @RequestParam int quantity,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Retrieve HoaDonEntity using hoaDonId
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (!optionalHoaDon.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return "redirect:/hoaDon/getAll";
            }
            HoaDonEntity hoaDon = optionalHoaDon.get();
            Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
            if (!optionalSanPhamChiTiet.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
                return "redirect:/addToCart/" + hoaDonId;
            }
            SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();

            // Validate quantity
            if (quantity <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Số lượng sản phẩm phải là số dương");
                return "redirect:/addToCart/" + hoaDonId;
            }

            HoaDonChiTietId primaryKey = new HoaDonChiTietId(hoaDonId, sanPhamChiTietId);
            Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);

            int existingQuantity = 0;
            if (optionalHoaDonChiTiet.isPresent()) {
                existingQuantity = optionalHoaDonChiTiet.get().getSoLuong();
            }

            // Check if the total requested quantity does not exceed the available stock
            if (existingQuantity + quantity > sanPhamChiTiet.getSoLuong()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
                return "redirect:/addToCart/" + hoaDonId;
            }

            // Update or create HoaDonChiTietEntity
            HoaDonChiTietEntity hoaDonChiTiet;
            if (optionalHoaDonChiTiet.isPresent()) {
                // Update existing entry
                hoaDonChiTiet = optionalHoaDonChiTiet.get();
                hoaDonChiTiet.setSoLuong(existingQuantity + quantity);
                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(existingQuantity + quantity)));
            } else {
                // Create new entry
                hoaDonChiTiet = new HoaDonChiTietEntity();
                hoaDonChiTiet.setHoaDon(hoaDon);
                hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                hoaDonChiTiet.setSoLuong(quantity);
                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
                hoaDonChiTiet.setCreateDate(LocalDate.now());
            }
            // Save or update HoaDonChiTietEntity
            hoaDonCTRepository.save(hoaDonChiTiet);

            // Reduce product quantity in inventory
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
            chiTietSPRepository.save(sanPhamChiTiet);

            // Update total amount in HoaDonEntity
            BigDecimal tongTien = hoaDon.getTongTien().add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
            hoaDon.setTongTien(tongTien);
            hoaDonRepository.save(hoaDon);

            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào hóa đơn thành công");
            return "redirect:/addToCart/" + hoaDonId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            return "redirect:/addToCart/" + hoaDonId;
        }
    }

    //    @PostMapping("/delete/{id}")
//    public String deleteHoaDon(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
//        Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(id);
//        if (!optionalHoaDon.isPresent()) {
//            redirectAttributes.addFlashAttribute("error", "Hóa đơn này không tồn tại");
//            return "redirect:/hoaDon/getAll";
//        }
//        hoaDonRepository.deleteById(id);
//        return "redirect:/hoaDon/getAll";
//    }
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

    //        @PostMapping("/sanphamct/delete/{hoaDonId}/{sanPhamCTid}")
//        public ResponseEntity<Map<String, String>> deleteSanPhamCT(@PathVariable("hoaDonId") UUID hoaDonId,
//                                                                   @PathVariable("sanPhamCTid") UUID sanPhamCTid) {
//            Map<String, String> response = new HashMap<>();
//            try {
//                // Kiểm tra xem hóa đơn có tồn tại không
//                Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//                if (!optionalHoaDon.isPresent()) {
//                    response.put("message", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//                }
//                HoaDonEntity hoaDon = optionalHoaDon.get();
//
//                // Kiểm tra xem sản phẩm chi tiết có tồn tại trong hóa đơn không
//                HoaDonChiTietPK primaryKey = new HoaDonChiTietPK();
//                primaryKey.setHoaDonId(hoaDonId);
//                primaryKey.setSanPhamChiTietId(sanPhamCTid);
//                Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);
//                if (!optionalHoaDonChiTiet.isPresent()) {
//                    response.put("message", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamCTid + " trong hóa đơn này");
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//                }
//
//                HoaDonChiTietEntity hoaDonChiTiet = optionalHoaDonChiTiet.get();
//
//                // Xóa sản phẩm chi tiết khỏi hóa đơn
//                hoaDonCTRepository.delete(hoaDonChiTiet);
//
//                // Cập nhật tổng tiền trong hóa đơn
//                BigDecimal tongTienMoi = hoaDon.getTongTien().subtract(hoaDonChiTiet.getThanhTien());
//                hoaDon.setTongTien(tongTienMoi);
//                hoaDonRepository.save(hoaDon);
//
//                response.put("message", "Sản phẩm chi tiết đã được xóa thành công");
//                return ResponseEntity.ok(response);
//            } catch (Exception e) {
//                response.put("message", "Đã xảy ra lỗi, vui lòng thử lại");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//            }
//        }
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

//    @PostMapping("/replaceProduct/{hoaDonId}")
//    public ResponseEntity<Map<String, String>> replaceProductInHoaDon(@PathVariable UUID hoaDonId,
//                                                                      @RequestParam UUID oldSanPhamChiTietId,
//                                                                      @RequestParam UUID newSanPhamChiTietId,
//                                                                      @RequestParam int quantity) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            // Retrieve HoaDonEntity using hoaDonId
//            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//            if (!optionalHoaDon.isPresent()) {
//                response.put("message", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//            HoaDonEntity hoaDon = optionalHoaDon.get();
//
//            // Retrieve old SanPhamChiTietEntity using oldSanPhamChiTietId
//            Optional<SanPhamChiTietEntity> optionalOldSanPhamChiTiet = chiTietSPRepository.findById(oldSanPhamChiTietId);
//            if (!optionalOldSanPhamChiTiet.isPresent()) {
//                response.put("message", "Không tìm thấy sản phẩm chi tiết với ID: " + oldSanPhamChiTietId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//            SanPhamChiTietEntity oldSanPhamChiTiet = optionalOldSanPhamChiTiet.get();
//
//            // Check if the old product detail is part of the invoice
//            HoaDonChiTietPK oldPrimaryKey = new HoaDonChiTietPK();
//            oldPrimaryKey.setHoaDonId(hoaDonId);
//            oldPrimaryKey.setSanPhamChiTietId(oldSanPhamChiTietId);
//            Optional<HoaDonChiTietEntity> optionalOldHoaDonChiTiet = hoaDonCTRepository.findById(oldPrimaryKey);
//            if (!optionalOldHoaDonChiTiet.isPresent()) {
//                response.put("message", "Sản phẩm chi tiết không thuộc về hóa đơn với ID: " + hoaDonId);
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//            HoaDonChiTietEntity oldHoaDonChiTiet = optionalOldHoaDonChiTiet.get();
//
//            // Retrieve new SanPhamChiTietEntity using newSanPhamChiTietId
//            Optional<SanPhamChiTietEntity> optionalNewSanPhamChiTiet = chiTietSPRepository.findById(newSanPhamChiTietId);
//            if (!optionalNewSanPhamChiTiet.isPresent()) {
//                response.put("message", "Không tìm thấy sản phẩm chi tiết với ID: " + newSanPhamChiTietId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//            SanPhamChiTietEntity newSanPhamChiTiet = optionalNewSanPhamChiTiet.get();
//
//            if (quantity <= 0) {
//                response.put("message", "Số lượng sản phẩm phải là số dương");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            // Check if enough quantity is available for the new product detail
//            if (quantity > newSanPhamChiTiet.getSoLuong()) {
//                response.put("message", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//            int oldQuantity = oldHoaDonChiTiet.getSoLuong();
//            BigDecimal oldTotal = oldHoaDonChiTiet.getThanhTien();
//            chiTietSPRepository.delete(oldHoaDonChiTiet);
//
//            // Update stock for the new product detail
//            if (quantity > newSanPhamChiTiet.getSoLuong()) {
//                response.put("message", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//            }
//
//            // Create new HoaDonChiTietEntity instance with the new product detail
//            HoaDonChiTietPK newPrimaryKey = new HoaDonChiTietPK();
//            newPrimaryKey.setHoaDonId(hoaDonId);
//            newPrimaryKey.setSanPhamChiTietId(newSanPhamChiTietId);
//            HoaDonChiTietEntity newHoaDonChiTiet = new HoaDonChiTietEntity();
//            newHoaDonChiTiet.setId(newPrimaryKey);
//            newHoaDonChiTiet.setHoaDon(hoaDon);
//            newHoaDonChiTiet.setSanPhamChiTiet(newSanPhamChiTiet);
//            newHoaDonChiTiet.setSoLuong(quantity);
//            newHoaDonChiTiet.setThanhTien(newSanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
//            newHoaDonChiTiet.setCreateDate(LocalDate.now());
//
//            hoaDonCTRepository.save(newHoaDonChiTiet);
//
//            // Update total amount in HoaDonEntity
//            BigDecimal newTotal = newHoaDonChiTiet.getThanhTien();
//            BigDecimal updatedTotal = hoaDon.getTongTien().subtract(oldTotal).add(newTotal);
//            hoaDon.setTongTien(updatedTotal);
//            hoaDonRepository.save(hoaDon);
//
//            response.put("message", "Sản phẩm chi tiết đã được thay thế thành công trong hóa đơn");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("message", "Đã xảy ra lỗi: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

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
            return "redirect:/hoaDon/getAll";
        }

        HoaDonEntity hoaDon = optionalHoaDon.get();
        List<HoaDonChiTietEntity> chiTietHoaDon = hoaDon.getHoaDonChiTiets();

        // Kiểm tra nếu hóa đơn không có sản phẩm
        if (chiTietHoaDon.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hóa đơn không có sản phẩm để thanh toán");
            return "redirect:/hoaDon/getAll";
        }
        model.addAttribute("hoaDon", hoaDon);
        return "admin/adminWeb/ThanhToan";
    }

    //    @PostMapping("/payment/{hoaDonId}")
//    public String processPayment(
//            @PathVariable("hoaDonId") UUID hoaDonId,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            System.out.println("HoaDonId: " + hoaDonId);
//            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//            if (optionalHoaDon.isEmpty()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                return "redirect:/hoaDon/getAll"; // Chuyển hướng đến URL phù hợp
//            }
//
//            HoaDonEntity hoaDon = optionalHoaDon.get();
//            boolean errorOccurred = false;
//
//            for (HoaDonChiTietEntity chiTiet : hoaDon.getHoaDonChiTiets()) {
//                UUID sanPhamChiTietId = chiTiet.getId().getSanPhamChiTietId();
//                int soLuongMua = chiTiet.getSoLuong();
//                Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
//                if (optionalSanPhamChiTiet.isPresent()) {
//                    SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();
//                    int soLuongHienTai = sanPhamChiTiet.getSoLuong();
//
//                    if (soLuongMua > soLuongHienTai) {
//                        redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm " + sanPhamChiTiet.getId() + " không đủ số lượng để thanh toán");
//                        errorOccurred = true;
//                        break;
//                    }
//                    sanPhamChiTiet.setSoLuong(soLuongHienTai - soLuongMua);
//                    chiTietSPRepository.save(sanPhamChiTiet);
//                } else {
//                    redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
//                    errorOccurred = true;
//                    break;
//                }
//            }
//            if (!errorOccurred) {
//                hoaDon.setTrangThai(1);
//                hoaDon.setNgayThanhToan(LocalDateTime.now().withNano(0));
//                hoaDonRepository.save(hoaDon);
//                redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn thành công");
//            }
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Chuỗi UUID không hợp lệ cho hoaDonId");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại");
//        }
//
//        return "redirect:/hoaDon/getAll";
//    }
//    @PostMapping("/payment/{hoaDonId}")
//    public String processPayment(
//            @PathVariable("hoaDonId") UUID hoaDonId,
//            @RequestParam(required = false) String tenKhachHang,
//            @RequestParam(required = false) String sdt,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
//            if (optionalHoaDon.isEmpty()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
//                return "redirect:/hoaDon/getAll";
//            }
//
//            HoaDonEntity hoaDon = optionalHoaDon.get();
//
//            // Update hoaDon with payment details
////            hoaDon.setTrangThai(1); // Mark as paid
//            hoaDon.setNgayThanhToan(LocalDate.from(LocalDateTime.now().withNano(0)));
//            hoaDonRepository.save(hoaDon); // Save the updated hoaDon
//
//            // Handling UserEntity
//            UserEntity userEntity;
//            if (tenKhachHang != null && !tenKhachHang.trim().isEmpty() && sdt != null && !sdt.trim().isEmpty()) {
//                userEntity = new UserEntity();
//                userEntity.setTen(tenKhachHang);
//                userEntity.setSdt(sdt);
//                // Set other fields if necessary
//
//                try {
//                    userEntity = userRepository.save(userEntity);
//                    logger.info("Đã lưu thông tin userEntity: " + userEntity);
//                } catch (Exception e) {
//                    logger.error("Lỗi khi lưu userEntity: " + e.getMessage(), e);
//                }
//            } else {
//                userEntity = userRepository.findById(UUID.fromString("FD4D4FCB-5F43-47EC-97FA-7D7A97DC1F8E")).orElseGet(() -> {
//                    UserEntity defaultUser = new UserEntity();
//                    defaultUser.setId(UUID.fromString("FD4D4FCB-5F43-47EC-97FA-7D7A97DC1F8E"));
//                    defaultUser.setTen("Khách lẻ");
//                    return userRepository.save(defaultUser);
//                });
//            }
//
//            hoaDon.setUser(userEntity);
//            hoaDonRepository.save(hoaDon);
//
//            redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn thành công");
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Chuỗi UUID không hợp lệ cho hoaDonId");
//            logger.error("Chuỗi UUID không hợp lệ cho hoaDonId: " + hoaDonId);
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại");
//            logger.error("Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage(), e);
//        }
//
//        return "redirect:/hoaDon/getAll"; // Redirect to appropriate URL for displaying all invoices
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

            UserEntity userEntity = null;
            if (tenKhachHang != null && !tenKhachHang.trim().isEmpty() && sdt != null && !sdt.trim().isEmpty()) {
                List<UserEntity> userEntities = userRepository.findByTenAndSdt(tenKhachHang, sdt);
                if (!userEntities.isEmpty()) {
                    userEntity = userEntities.get(0); // Sử dụng khách hàng hiện có
                }
                // Nếu không tìm thấy khách hàng, tạo mới
                if (userEntity == null) {
                    userEntity = new UserEntity();
                    userEntity.setTaiKhoan("");
                    userEntity.setHo("");
                    userEntity.setTenDem("");
                    userEntity.setTen(tenKhachHang);
                    userEntity.setSdt(sdt);
                    userEntity.setGioiTinh(1);
                    userEntity.setTrangThai(1);
                    userEntity = userRepository.save(userEntity);
                }
            } else {
                // Xử lý trường hợp không có tên và số điện thoại, gán cho khách lẻ
                userEntity = userRepository.findByTen("Khách lẻ").orElseGet(() -> {
                    UserEntity defaultUser = new UserEntity();
                    defaultUser.setTen("Khách lẻ");
                    defaultUser.setTrangThai(0); // Thiết lập các thuộc tính khác nếu cần
                    return userRepository.save(defaultUser);
                });
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

