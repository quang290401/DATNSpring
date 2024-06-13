// scripts.js

var url = document.location.pathname;
var id = url.substring(url.lastIndexOf('/') + 1);
$(document).ready(function() {
    // Hàm để lấy chi tiết sản phẩm
    function fetchProductDetails(id) {
        $.ajax({
            url: `/api/SPCT/Detail/` + id, // Thay thế bằng endpoint API của bạn
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                // Ghi dữ liệu vào console để gỡ lỗi
                console.log('Dữ liệu sản phẩm:', data);

                // Kiểm tra xem URL hình ảnh có chính xác không
                if (data.hinhAnh && data.hinhAnh.duongDan) {
                    let imageUrl = data.hinhAnh.duongDan;
                    console.log('URL hình ảnh:', imageUrl);

                    // Nếu đường dẫn không bắt đầu bằng http:// hoặc https://, thêm tiền tố
                    if (!imageUrl.startsWith('http://') && !imageUrl.startsWith('https://')) {
                        imageUrl = `http://localhost:8080/${imageUrl}`;
                    }

                    $('#productImage').attr('src', imageUrl);
                } else {
                    console.error('URL hình ảnh không hợp lệ:', data.hinhAnh);
                }

                $('#productName').text(data.sanPham.tenSanPham);
                $('#productOldPrice').text(`$${(data.giaSanPham * 1.2).toFixed(2)}`); // Giả sử giá cũ cao hơn 20%
                $('#productPrice').text(`$${data.giaSanPham.toFixed(2)}`);
                $('#productDescription').html(`Mô Tả : ${data.moTa}<br>Kích Cỡ : ${data.kichCo.tenKichCo}<br>NSX : ${data.nsx.ten}`);
            },
            error: function(err) {
                console.error('Lỗi khi lấy chi tiết sản phẩm:', err);
            }
        });
    }

    // Gọi ví dụ để lấy chi tiết sản phẩm cho một ID sản phẩm cụ thể
    fetchProductDetails(id); // Thay thế bằng ID sản phẩm thực tế
});

$(document).ready(function() {
    // Function to fetch top 4 products
    function fetchTop4Products() {
        $.ajax({
            url: '/api/SPCT/top4', // Endpoint API của bạn
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                // Ghi dữ liệu vào console để gỡ lỗi
                console.log('Dữ liệu sản phẩm:', data);

                // Lặp qua danh sách sản phẩm và tạo HTML cho mỗi sản phẩm
                var productContainer = $('#productContainer');
                productContainer.empty(); // Xóa nội dung cũ (nếu có)

                data.forEach(function(product) {
                    var productCard = `
                            <div class="col mb-5">
                                <div class="card h-100 card-custom">
                                    <!-- Product image-->
                                    <img class="card-img-top card-img-top-custom" src="http://localhost:8080/${product.hinhAnh.duongDan}" alt="..."/>
                                    <!-- Product details-->
                                    <div class="card-body p-4 card-body-custom">
                                        <div class="text-center">
                                            <!-- Product name-->
                                            <h5 class="fw-bolder">${product.sanPham.tenSanPham}</h5>
                                            <!-- Product price-->
                                            <div>${product.giaSanPham}</div>
                                        </div>
                                    </div>
                                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent card-footer-custom">
                                        <div class="text-center">
                                            <a class="btn btn-outline-dark mt-auto" href="/DetailSP/${product.id}">View options</a>
                                        </div>
                                    </div>
                                </div>
                            </div>`;
                    productContainer.append(productCard);
                });
            },
            error: function(err) {
                console.error('Lỗi khi lấy danh sách sản phẩm:', err);
            }
        });
    }

    // Gọi hàm fetchTop4Products khi trang được tải
    fetchTop4Products();
});
