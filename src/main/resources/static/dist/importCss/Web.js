let currentPage = 1;
let pageSize = 8;

function fetchProducts(pageNo) {
    $.ajax({
        url: `/api/SPCT?pageNo=${pageNo}&pageSize=${pageSize}`,
        method: 'GET',
        dataType: 'json',
        success: function(response) {
            console.log('Data received from API:', response); // Kiểm tra dữ liệu nhận được
            var container = $('.row-cols-2.row-cols-md-3.row-cols-xl-4.justify-content-center');
            container.empty(); // Xóa nội dung cũ

            // Lấy mảng sản phẩm từ dữ liệu nhận được
            var products = response.content;

            // Kiểm tra nếu products là một mảng
            if (Array.isArray(products)) {
                products.forEach(function(product) { // Sửa tên biến thành product
                    var productId = product.id; // Lấy id của sản phẩm
                    var productHtml = `
                        <div class="col mb-5">
                            <div class="card h-100">
                                <!-- Product image-->
                                <img class="card-img-top" src="${product.hinhAnh.duongDan}" alt="...">
                                <!-- Product details-->
                                <div class="card-body p-4">
                                    <div class="text-center">
                                        <!-- Product name-->
                                        <h5 class="fw-bolder">${product.sanPham.tenSanPham}</h5>
                                        <!-- Product price-->
                                        ${product.giaSanPham.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})}
                                    </div>
                                </div>
                                <!-- Product actions-->
                                <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                    <div class="text-center"><a class="btn btn-outline-dark mt-auto view-options" href="#" data-product-id="${productId}">View options</a></div>
                                </div>
                            </div>
                        </div>
                    `;
                    container.append(productHtml);
                });

                // Cập nhật liên kết phân trang
                updatePagination(response);
            } else {
                console.error('Expected an array but got:', products);
                container.append('<p class="text-danger">Error: Expected an array but got invalid data format.</p>');
            }
        },
        error: function(err) {
            console.error('Error fetching products:', err);
            var container = $('.row-cols-2.row-cols-md-3.row-cols-xl-4.justify-content-center');
            container.empty(); // Xóa nội dung cũ
            container.append('<p class="text-danger">Error fetching products. Please try again later.</p>');
        }
    });
}

function updatePagination(response) {
    var paginationContainer = $('.pagination');
    paginationContainer.empty(); // Xóa nội dung cũ

    var totalPages = response.totalPages;
    var currentPage = response.pageable.pageNumber + 1; // Cập nhật số trang hiện tại

    for (var i = 1; i <= totalPages; i++) {
        var activeClass = (i === currentPage) ? 'active' : '';
        var pageItem = `
        <li class="page-item ${activeClass}">
          <a class="page-link" href="#" data-page="${i}">${i}</a>
        </li>
      `;
        paginationContainer.append(pageItem);
    }
}

$(document).ready(function() {
    fetchProducts(currentPage-1);

    // Bắt sự kiện click vào liên kết phân trang
    $(document).on('click', '.pagination .page-link', function(e) {
        e.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>

        var pageNo = $(this).data('page'); // Lấy số trang từ thuộc tính data
        fetchProducts(pageNo-1); // Tải lại sản phẩm với trang mới
    });

    // Bắt sự kiện click vào liên kết View options
    $(document).on('click', '.view-options', function(e) {
        e.preventDefault(); // Ngăn chặn hành vi mặc định của thẻ <a>

        var productId = $(this).data('product-id'); // Lấy id từ thuộc tính data

        // Chuyển hướng đến trang chi tiết sản phẩm
        window.location.href = '/DetailSP/' + productId;
    });
});