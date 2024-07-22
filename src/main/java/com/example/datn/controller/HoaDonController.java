package com.example.datn.controller;

import com.example.datn.Repository.*;
import com.example.datn.entity.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.example.datn.DTO.HoaDonChiTietDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;

import java.util.stream.Collectors;

@CrossOrigin("*")
@Controller
@RequestMapping("/hoaDon")
public class HoaDonController {
    private static final Logger logger = LoggerFactory.getLogger(HoaDonController.class);

    private final HoaDonRepository hoaDonRepository;
    final UserRepository userRepository;
    private final ChiTietSPRepository chiTietSPRepository;
    private final HoaDonCTRepository hoaDonCTRepository;

    public HoaDonController(HoaDonRepository hoaDonRepository, UserRepository userRepository, ChiTietSPRepository chiTietSPRepository, HoaDonCTRepository hoaDonCTRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.userRepository = userRepository;
        this.chiTietSPRepository = chiTietSPRepository;
        this.hoaDonCTRepository = hoaDonCTRepository;
    }

    @GetMapping("/getAll")
    public String getAllHoaDon(Model model) {
        List<HoaDonEntity> danhSachHoaDon = hoaDonRepository.findAll();
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);
        List<UserEntity> listUser = userRepository.findAll();
        model.addAttribute("danhSachHoaDon", danhSachHoaDon);
        List<HoaDonEntity> danhSachHoaDonDaThanhToan = danhSachHoaDon.stream()
                .filter(hd -> hd.getTrangThai() == 1) // Giả sử trạng thái 1 là đã thanh toán
                .collect(Collectors.toList());
        model.addAttribute("danhSachHoaDonDaThanhToan", danhSachHoaDonDaThanhToan);

        List<HoaDonEntity> danhSachHoaDonChuaThanhToan = danhSachHoaDon.stream()
                .filter(hd -> hd.getTrangThai() == 0) // Giả sử trạng thái 0 là chưa thanh toán
                .collect(Collectors.toList());
        model.addAttribute("danhSachHoaDonChuaThanhToan", danhSachHoaDonChuaThanhToan);

        return "admin/adminWeb/HoaDon";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("hoaDon", new HoaDonEntity());
        return "admin/adminWeb/HoaDon"; // Adjust template name as per your project structure
    }

    @PostMapping("/create")
    public String createHoaDon(@ModelAttribute("hoaDon") HoaDonEntity hoaDon, RedirectAttributes redirectAttributes) {
        hoaDon.setTrangThai(0);
        if (hoaDon.getTongTien() == null) {
            hoaDon.setTongTien(BigDecimal.ZERO);
        }
        hoaDonRepository.save(hoaDon);
        return "redirect:/hoaDon/getAll";
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

    @GetMapping("/trangThai/{hoaDonId}")
    public ResponseEntity<Integer> getInvoiceStatus(@PathVariable UUID hoaDonId) {
        Integer trangThai = hoaDonRepository.findTrangThaiById(hoaDonId);
        if (trangThai != null) {
            return ResponseEntity.ok(trangThai);
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

        // Chuyển đổif danh sách chi tiết hóa đơn sang DTO
        List<HoaDonChiTietDTO> chiTietDTOs = chiTietEntities.stream()
                .map(entity -> new HoaDonChiTietDTO(
                        entity.getSanPhamChiTiet().getId(),
                        entity.getSanPhamChiTiet().getSanPham().getTenSanPham(),
                        entity.getSanPhamChiTiet().getMauSac().getTen(),
                        entity.getSanPhamChiTiet().getKichCo().getTenKichCo(),
                        entity.getSoLuong(),
                        entity.getSanPhamChiTiet().getGiaSanPham(),
                        entity.getThanhTien()
                ))
                .collect(Collectors.toList());

        // Chuyển đổi danh sách DTO thành chuỗi JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(chiTietDTOs);

            // Thiết lập header Content-Type là application/json
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(json, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            // Xử lý lỗi khi chuyển đổi sang JSON
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

            // Retrieve SanPhamChiTietEntity using sanPhamChiTietId
            Optional<SanPhamChiTietEntity> optionalSanPhamChiTiet = chiTietSPRepository.findById(sanPhamChiTietId);
            if (!optionalSanPhamChiTiet.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm chi tiết với ID: " + sanPhamChiTietId);
                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
            }
            SanPhamChiTietEntity sanPhamChiTiet = optionalSanPhamChiTiet.get();

            // Validate quantity
            if (quantity <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Số lượng sản phẩm phải là số dương");
                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
            }

            // Check if enough quantity is available
            if (quantity > sanPhamChiTiet.getSoLuong()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để thêm vào hóa đơn");
                return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
            }

            // Reduce product quantity in inventory
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - quantity);
            chiTietSPRepository.save(sanPhamChiTiet);

            // Retrieve HoaDonChiTietEntity if it exists
            HoaDonChiTietPK primaryKey = new HoaDonChiTietPK();
            primaryKey.setHoaDonId(hoaDonId);
            primaryKey.setSanPhamChiTietId(sanPhamChiTietId);
            Optional<HoaDonChiTietEntity> optionalHoaDonChiTiet = hoaDonCTRepository.findById(primaryKey);

            HoaDonChiTietEntity hoaDonChiTiet;
            if (optionalHoaDonChiTiet.isPresent()) {
                // Update existing HoaDonChiTietEntity
                hoaDonChiTiet = optionalHoaDonChiTiet.get();
                int newQuantity = hoaDonChiTiet.getSoLuong() + quantity;
                if (newQuantity > sanPhamChiTiet.getSoLuong()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không đủ số lượng để cập nhật hóa đơn");
                    return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect
                }
                hoaDonChiTiet.setSoLuong(newQuantity);
                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(newQuantity)));
            } else {
                // Create new HoaDonChiTietEntity instance
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

            // Update total amount in HoaDonEntity
            BigDecimal tongTien = hoaDon.getTongTien().add(sanPhamChiTiet.getGiaSanPham().multiply(BigDecimal.valueOf(quantity)));
            hoaDon.setTongTien(tongTien);
            hoaDonRepository.save(hoaDon);

            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm sản phẩm vào hóa đơn thành công");
            return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect after success
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            return "redirect:/hoaDon/addToCart/" + hoaDonId; // Retain hoaDonId in the redirect if an exception occurs
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
            return "redirect:/hoaDon/getAll";
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
        return "redirect:/hoaDon/getAll";
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
            hoaDon.setTrangThai(hoaDon.getTrangThai() + 1);
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
    @PostMapping("/payment/{hoaDonId}")
    public String processPayment(
            @PathVariable("hoaDonId") UUID hoaDonId,
            @RequestParam(required = false) String tenKhachHang,
            @RequestParam(required = false) String sdt,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (optionalHoaDon.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hóa đơn với ID: " + hoaDonId);
                return "redirect:/hoaDon/getAll";
            }

            HoaDonEntity hoaDon = optionalHoaDon.get();

            // Update hoaDon with payment details
            hoaDon.setTrangThai(1); // Mark as paid
            hoaDon.setNgayThanhToan(LocalDateTime.now().withNano(0));
            hoaDonRepository.save(hoaDon); // Save the updated hoaDon

            // Handling UserEntity
            UserEntity userEntity;
            if (tenKhachHang != null && !tenKhachHang.trim().isEmpty() && sdt != null && !sdt.trim().isEmpty()) {
                userEntity = new UserEntity();
                userEntity.setTen(tenKhachHang);
                userEntity.setSdt(sdt);
                // Set other fields if necessary

                try {
                    userEntity = userRepository.save(userEntity);
                    logger.info("Đã lưu thông tin userEntity: " + userEntity);
                } catch (Exception e) {
                    logger.error("Lỗi khi lưu userEntity: " + e.getMessage(), e);
                }
            } else {
                userEntity = userRepository.findById(UUID.fromString("FD4D4FCB-5F43-47EC-97FA-7D7A97DC1F8E")).orElseGet(() -> {
                    UserEntity defaultUser = new UserEntity();
                    defaultUser.setId(UUID.fromString("FD4D4FCB-5F43-47EC-97FA-7D7A97DC1F8E"));
                    defaultUser.setTen("Khách lẻ");
                    return userRepository.save(defaultUser);
                });
            }

            hoaDon.setUser(userEntity);
            hoaDonRepository.save(hoaDon);

            redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn thành công");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chuỗi UUID không hợp lệ cho hoaDonId");
            logger.error("Chuỗi UUID không hợp lệ cho hoaDonId: " + hoaDonId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại");
            logger.error("Đã xảy ra lỗi khi xử lý thanh toán: " + e.getMessage(), e);
        }

        return "redirect:/hoaDon/getAll"; // Redirect to appropriate URL for displaying all invoices
    }

    @GetMapping("/exportPDF/{hoaDonId}")
    public ResponseEntity<InputStreamResource> exportToPdf(@PathVariable("hoaDonId") UUID hoaDonId) {
        try {
            Optional<HoaDonEntity> optionalHoaDon = hoaDonRepository.findById(hoaDonId);
            if (optionalHoaDon.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            HoaDonEntity hoaDon = optionalHoaDon.get();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Sử dụng phông chữ TrueType nếu cần hỗ trợ nhiều ký tự hơn
            PDType0Font font = PDType0Font.load(document, new File("path/to/your/font.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(100, 700);

                contentStream.showText("Hóa Đơn ID: " + hoaDon.getId());
                contentStream.newLine();
                contentStream.showText("Ngày Tạo: " + (hoaDon.getCreateDate() != null ? hoaDon.getCreateDate().toString() : "Chưa có"));
                contentStream.newLine();
                contentStream.showText("Ngày Thanh Toán: " + (hoaDon.getNgayThanhToan() != null ? hoaDon.getNgayThanhToan().toString() : "Chưa có"));
                contentStream.newLine();
                contentStream.showText("Sản phẩm: " +
                        (hoaDon.getSanPhamChiTiet() != null ?
                                hoaDon.getSanPhamChiTiet().getSanPham() + " " +
                                        hoaDon.getSanPhamChiTiet().getKichCo() + " " +
                                        hoaDon.getSanPhamChiTiet().getMauSac() : "Không có thông tin"));
                contentStream.newLine();
                contentStream.showText("Khách hàng: " + (hoaDon.getUser() != null ? hoaDon.getUser().getTen() : "Khách lẻ"));
                contentStream.newLine();
                contentStream.showText("Tổng tiền: " + hoaDon.getTongTien());

                contentStream.endText();
            }

            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=hoaDon_" + hoaDonId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}