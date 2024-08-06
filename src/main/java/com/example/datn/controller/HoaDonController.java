package com.example.datn.controller;
import com.example.datn.dto.HoaDonCTDTO;
import com.example.datn.dto.ProductDetailDTO;
import com.example.datn.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.datn.Repository.*;
import com.example.datn.entity.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class  HoaDonController {
    @Autowired
    private TrangThaiHDRepository trangThaiHDRepository;
    private final HoaDonRepository hoaDonRepository;
    final UsersRepository userRepository;
    private final ChiTietSPRepository chiTietSPRepository;
    private final HoaDonCTRepository hoaDonCTRepository;

    public HoaDonController(HoaDonRepository hoaDonRepository, UsersRepository userRepository, ChiTietSPRepository chiTietSPRepository, HoaDonCTRepository hoaDonCTRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.userRepository = userRepository;
        this.chiTietSPRepository = chiTietSPRepository;
        this.hoaDonCTRepository = hoaDonCTRepository;
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

        // Lọc các hóa đơn chưa thanh toán
        List<HoaDonEntity> danhSachHoaDonChuaThanhToan = danhSachHoaDon.stream()
                .filter(hd -> hd.getTrangThaiHD() != null && hd.getTrangThaiHD().getTrangThai().equals("0"))
                .collect(Collectors.toList());
        model.addAttribute("danhSachHoaDonChuaThanhToan", danhSachHoaDonChuaThanhToan);

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
            Optional<TrangThaiHDEntity> chuaThanhToanOpt = trangThaiHDRepository.findByTen("Chưa Thanh Toán");
            if (chuaThanhToanOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Trạng thái chưa thanh toán không tìm thấy.");
                return "redirect:/getAll";
            }
            TrangThaiHDEntity chuaThanhToan = chuaThanhToanOpt.get();
            hoaDon.setTrangThaiHD(chuaThanhToan);
            hoaDon.setCreateDate(LocalDate.now());
//            hoaDon.setNgayThanhToan(LocalDateTime.now());
            if (hoaDon.getTongTien() == null) {
                hoaDon.setTongTien(BigDecimal.ZERO);
            }
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("success", "Tạo hóa đơn thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi tạo hóa đơn: " + e.getMessage());
        }
        return "redirect:/getAll";
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
        HoaDonChiTietPK id = new HoaDonChiTietPK(hoaDonId, sanPhamChiTietId);
        Optional<HoaDonChiTietEntity> optionalChiTiet = hoaDonCTRepository.findById(id);
        if (optionalChiTiet.isPresent()) {
            HoaDonChiTietEntity chiTiet = optionalChiTiet.get();
            chiTiet.setSoLuong(soLuong);
            hoaDonCTRepository.save(chiTiet);
            return ResponseEntity.ok("Số lượng đã được cập nhật.");
        } else {
            return ResponseEntity.status(404).body("Chi tiết hóa đơn không tồn tại.");
        }
    }

    @PostMapping("/deleteDetail/{hoaDonId}/{sanPhamChiTietId}")
    public ResponseEntity<String> deleteDetail(@PathVariable UUID hoaDonId, @PathVariable UUID sanPhamChiTietId) {
        HoaDonChiTietPK id = new HoaDonChiTietPK(hoaDonId, sanPhamChiTietId);
        boolean isRemoved = hoaDonCTRepository.existsById(id);
        if (!isRemoved) {
            return ResponseEntity.status(404).body("Chi tiết hóa đơn không tồn tại.");
        }
        hoaDonCTRepository.deleteById(id);
        return ResponseEntity.ok("Chi tiết hóa đơn đã được xóa.");
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

    //    @GetMapping("/getProductDetail/{hoaDonId}")
//    public ResponseEntity<String> getProductDetail(
//            @PathVariable("hoaDonId") String hoaDonIdStr,
//            @PathVariable("sanPhamChiTietId") String sanPhamChiTietIdStr) {
//
//        UUID hoaDonId;
//        UUID sanPhamChiTietId;
//
//        try {
//            hoaDonId = UUID.fromString(hoaDonIdStr);
//            sanPhamChiTietId = UUID.fromString(sanPhamChiTietIdStr);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Định dạng UUID không hợp lệ");
//        }
//
//        // Tìm chi tiết hóa đơn theo ID
//        HoaDonChiTietPK primaryKey = new HoaDonChiTietPK(hoaDonId, sanPhamChiTietId);
//        Optional<HoaDonChiTietEntity> chiTietOptional = hoaDonCTRepository.findById(primaryKey);
//
//        if (!chiTietOptional.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        HoaDonChiTietEntity chiTiet = chiTietOptional.get();
//        SanPhamChiTietEntity sanPhamChiTiet = chiTiet.getSanPhamChiTiet();
//
//        ProductDetailDTO productDetailDTO = new ProductDetailDTO(
//                sanPhamChiTiet.getId(),
//                sanPhamChiTiet.getSanPham()!= null ?sanPhamChiTiet.getSanPham().getTenSanPham(): "N/A",
//                sanPhamChiTiet.getMauSac() != null ? sanPhamChiTiet.getMauSac().getTen() : "N/A",
//                sanPhamChiTiet.getKichCo() != null ? sanPhamChiTiet.getKichCo().getTenKichCo() : "N/A",
//                chiTiet.getSoLuong(),
//                sanPhamChiTiet.getGiaSanPham(),
//                chiTiet.getThanhTien()
//        );
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String json = mapper.writeValueAsString(productDetailDTO);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//
//            return new ResponseEntity<>(json, headers, HttpStatus.OK);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // In ra thông tin chi tiết lỗi
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi chuyển đổi sang JSON");
//        }
//    }
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
        HoaDonChiTietPK primaryKey = new HoaDonChiTietPK(hoaDonId, sanPhamChiTietId);
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

    //@GetMapping("/getDetails/{hoaDonId}")
//public ResponseEntity<String> getDetails(@PathVariable("hoaDonId") String id) {
//    UUID hoaDonId;
//    try {
//        hoaDonId = UUID.fromString(id);
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.badRequest().body("Định dạng UUID không hợp lệ");
//    }
//
//    List<HoaDonChiTietEntity> chiTietEntities = hoaDonCTRepository.findByHoaDon_Id(hoaDonId);
//
//    if (chiTietEntities == null || chiTietEntities.isEmpty()) {
//        return ResponseEntity.notFound().build();
//    }
//
//    List<HoaDonCTDTO> chiTietDTOs = chiTietEntities.stream()
//            .map(entity -> new HoaDonCTDTO(
//                    entity.getSanPhamChiTiet().getId(),
//                    entity.getSanPhamChiTiet().getSanPham().getTenSanPham(),
//                    entity.getSanPhamChiTiet().getMauSac() != null ? entity.getSanPhamChiTiet().getMauSac().getTen() : "N/A",
//                    entity.getSanPhamChiTiet().getKichCo() != null ? entity.getSanPhamChiTiet().getKichCo().getTenKichCo() : "N/A",
//                    entity.getSoLuong(),
//                    entity.getSanPhamChiTiet().getGiaSanPham(),
//                    entity.getThanhTien()
//            ))
//            .collect(Collectors.toList());
//
//    ObjectMapper mapper = new ObjectMapper();
//    try {
//        String json = mapper.writeValueAsString(chiTietDTOs);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//
//        return new ResponseEntity<>(json, headers, HttpStatus.OK);
//    } catch (JsonProcessingException e) {
//        e.printStackTrace(); // In ra thông tin chi tiết lỗi
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi chuyển đổi sang JSON");
//    }
//}
//@GetMapping("/getDetails/{hoaDonId}")
//public ResponseEntity<String> getDetails(@PathVariable("hoaDonId") String id) {
//    UUID hoaDonId;
//    try {
//        hoaDonId = UUID.fromString(id);
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.badRequest().body("Định dạng UUID không hợp lệ");
//    }
//
//    List<HoaDonChiTietEntity> chiTietEntities = hoaDonCTRepository.findByHoaDon_Id(hoaDonId);
//
//    if (chiTietEntities == null || chiTietEntities.isEmpty()) {
//        return ResponseEntity.notFound().build();
//    }
//
//    List<HoaDonCTDTO> chiTietDTOs = chiTietEntities.stream()
//            .map(entity -> new HoaDonCTDTO(
//                    entity.getSanPhamChiTiet().getId(),
//                    entity.getSanPhamChiTiet().getSanPham().getTenSanPham(),
//                    entity.getSanPhamChiTiet().getMauSac() != null ? entity.getSanPhamChiTiet().getMauSac().getTen() : "N/A",
//                    entity.getSanPhamChiTiet().getKichCo() != null ? entity.getSanPhamChiTiet().getKichCo().getTenKichCo() : "N/A",
//                    entity.getSoLuong(),
//                    entity.getSanPhamChiTiet().getGiaSanPham(),
//                    entity.getThanhTien()
//            ))
//            .collect(Collectors.toList());
//
//    ObjectMapper mapper = new ObjectMapper();
//    try {
//        String json = mapper.writeValueAsString(chiTietDTOs);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
//
//        return new ResponseEntity<>(json, headers, HttpStatus.OK);
//    } catch (JsonProcessingException e) {
//        e.printStackTrace(); // In ra thông tin chi tiết lỗi
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi chuyển đổi sang JSON");
//    }
//}

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
//                return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//            SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();
//
//            // Validate quantity
//            if (quantity <= 0) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Số lượng sản phẩm phải là số dương");
//                return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//
//            // Check if enough quantity is available
//            if (quantity > sanPhamChiTiet.getSoLuong()) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
//                return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
//            }
//
//            // Reduce product quantity in inventory
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
//            chiTietSPRepository.save(sanPhamChiTiet);
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
//                    return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
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
//            // Save or update HoaDonChiTietEntity
//            hoaDonCTRepository.save(hoaDonChiTiet);
//
//            // Update total amount in HoaDonEntity
//            BigDecimal tongTien = hoaDon.getTongTien().add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
//            hoaDon.setTongTien(tongTien);
//            hoaDonRepository.save(hoaDon);
//
//            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào hóa đơn thành công");
//            return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect after success
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
//            return "redirect:/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect if an exception occurs
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
        HoaDonChiTietPK primaryKey = new HoaDonChiTietPK();
        primaryKey.setHoaDonId(hoaDonId);
        primaryKey.setSanPhamChiTietId(sanPhamChiTietId);
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
            hoaDonChiTiet.setId(primaryKey);
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

        // Xóa tất cả chi tiết hóa đơn
        hoaDonCTRepository.deleteAll(hoaDonChiTietList);

        // Xóa hóa đơn
        hoaDonRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Hóa đơn đã được xóa thành công và số lượng sản phẩm đã được cập nhật.");
        return "redirect:/getAll";
    }
    @PostMapping("/sanphamct/delete/{hoaDonId}/{sanPhamCTid}")
    public ResponseEntity<Map<String, String>> deleteSanPhamCT(@PathVariable("hoaDonId") UUID hoaDonId,
                                                               @PathVariable("sanPhamCTid") UUID sanPhamCTid) {
        Map<String, String> response = new HashMap<>();
        try {
            // Kiểm tra xem hóa đơn có tồn tại không
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (!optionalHoaDon.isPresent()) {
                response.put("message", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            HoaDonEntity hoaDon = optionalHoaDon.get();

            // Kiểm tra xem sản phẩm chi tiết có tồn tại trong hóa đơn không
            HoaDonChiTietPK primaryKey = new HoaDonChiTietPK();
            primaryKey.setHoaDonId(hoaDonId);
            primaryKey.setSanPhamChiTietId(sanPhamCTid);
            Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);
            if (!optionalHoaDonChiTiet.isPresent()) {
                response.put("message", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamCTid + " trong hóa đơn này");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            HoaDonChiTietEntity hoaDonChiTiet = optionalHoaDonChiTiet.get();

            // Cộng lại số lượng sản phẩm trong kho
            SanPhamChiTietEntity sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong());
            chiTietSPRepository.save(sanPhamChiTiet);

            // Xóa sản phẩm chi tiết khỏi hóa đơn
            hoaDonCTRepository.delete(hoaDonChiTiet);

            // Cập nhật tổng tiền trong hóa đơn
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
    @GetMapping("/trangThai/{hoaDonId}")
    public ResponseEntity<Map<String, String>> getInvoiceStatus(@PathVariable UUID hoaDonId) {
        Map<String, String> response = hoaDonRepository.findTrangThaiById(hoaDonId);
        if (response != null && !response.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
            Optional<TrangThaiHDEntity> daThanhToanOpt = trangThaiHDRepository.findByTen("Đã Thanh Toán");
            if (daThanhToanOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Trạng thái 'Đã Thanh Toán' không tìm thấy.");
                return "redirect:/getAll";
            }
            TrangThaiHDEntity daThanhToan = daThanhToanOpt.get();
            hoaDon.setTrangThaiHD(daThanhToan);
            hoaDon.setNgayThanhToan(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

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
    @PostMapping("/updateProductDetail")
    public String updateProductDetail(
            @RequestParam UUID hoaDonId,
            @RequestParam UUID sanPhamChiTietId,
            @RequestParam(required = false) String kichCo,
            @RequestParam(required = false) String mauSac,
            @RequestParam int soLuong,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (!optionalHoaDon.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return "redirect:/getAll";
            }
            HoaDonEntity hoaDon = optionalHoaDon.get();

            // Tìm sản phẩm chi tiết
            Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
            if (!optionalSanPhamChiTiet.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
                return "redirect:/addToCart/" + hoaDonId;
            }
            SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();
            // Tìm chi tiết hóa đơn
            HoaDonChiTietPK primaryKey = new HoaDonChiTietPK(hoaDonId, sanPhamChiTietId);
            Optional<HoaDonChiTietEntity> optionalChiTietHoaDon = hoaDonCTRepository.findById(primaryKey);
            if (!optionalChiTietHoaDon.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Chi tiết hóa đơn không tồn tại.");
                return "redirect:/addToCart/" + hoaDonId;
            }
            HoaDonChiTietEntity chiTietHoaDon = optionalChiTietHoaDon.get();
            // Cập nhật thông tin
            chiTietHoaDon.setSoLuong(soLuong);
            chiTietHoaDon.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(soLuong)));
            // Cập nhật kích cỡ và màu sắc nếu cần
            if (kichCo != null && !kichCo.trim().isEmpty()) {
                if (chiTietHoaDon.getSanPhamChiTiet().getKichCo() != null) {
                    chiTietHoaDon.getSanPhamChiTiet().getKichCo().setTenKichCo(kichCo);
                }
            }
            if (mauSac != null && !mauSac.trim().isEmpty()) {
                if (chiTietHoaDon.getSanPhamChiTiet().getMauSac() != null) {
                    chiTietHoaDon.getSanPhamChiTiet().getMauSac().setTen(mauSac);
                }
            }
            hoaDonCTRepository.save(chiTietHoaDon);
            // Cập nhật tổng tiền của hóa đơn
            BigDecimal tongTien = hoaDon.getTongTien()
                    .subtract(chiTietHoaDon.getThanhTien())
                    .add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(soLuong)));
            hoaDon.setTongTien(tongTien);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin sản phẩm chi tiết thành công.");
            return "redirect:/addToCart/" + hoaDonId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            return "redirect:/addToCart/" + hoaDonId;
        }
    }
}

