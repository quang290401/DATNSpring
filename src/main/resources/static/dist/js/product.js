let currentPage = 1;
let pageSize = 2;
let keySearch ="";

// JavaScript để hiển thị form cập nhật khi nhấn nút sửa

let product = {
    getAllProduct: function (pageNumber, pageSize, keySearch) {
        let url = `/api/products?pageNo=${pageNumber}&pageSize=${pageSize}`;
        if (keySearch) {
            url += `&nameProduct=${keySearch}`;
        }
        $.ajax({
            url: url,
            method: "GET",
            success: function (response, textStatus, xhr) {
                console.log(response)
                displayProduct(response.content);
                displayPagination(response.totalPages);
            },
            error: function (error) {
                alert("Fail");
            }
        });
    }
};


function displayProduct(data) {
    const tableBody = $("#productTableId");
    tableBody.empty();

    // Kiểm tra xem data có tồn tại và có phải là một đối tượng không
    if (data && typeof data === 'object') {
        for (let product of Object.values(data)) {
            let row = `<tr>
                                <td>${product.nameProduct}</td>
                                <td>${product.priceProduct}</td>
                                <td>${product.color}</td>
                                <td>${product.quantity}</td>
                                 <td>${product.status}</td>
                                 <td>
                            <a href="/product/edit/${product.id}" class="btn btn-success" role="button">Sửa</a>
                             <button class="btn btn-danger btn-delete" data-product-id="${product.id}">Xóa</button>


                            </tr>`;

            tableBody.append(row);

        }
        // Bắt sự kiện cho nút xóa
        $(".btn-delete").click(function () {
            // Lấy ID của sản phẩm từ thuộc tính data-product-id của nút xóa
            let productId = $(this).data("product-id");

            // Gửi yêu cầu xóa sản phẩm đến API
            deleteProduct(productId);
        });


        function deleteProduct(productId) {
            // Gửi yêu cầu DELETE đến API với ID của sản phẩm cần xóa
            $.ajax({
                url: `/api/products/${productId}`,
                type: 'DELETE',
                success: function (response) {
                    product.getAllProduct(currentPage - 1, pageSize);
                    console.log("Product deleted successfully");
                },
                error: function (xhr, status, error) {
                    // Xử lý khi xảy ra lỗi khi xóa sản phẩm
                    console.error("Error deleting product:", error);
                }
            });
        }
    }
}

product.getAllProduct(currentPage - 1, pageSize);
function searchProduct(){
    keySearch = $("#keySearch").val();
    currentPage = 1;
    product.getAllProduct(currentPage-1,pageSize,keySearch);
}

function displayPagination(totalPage) {
    const pagination = $(".pagination");
    pagination.empty();

    // Tạo nút "Previous"
    let previousBtn = `<li class="page-item"><a class="page-link" href="#" onclick="changePage(${currentPage - 1})">Previous</a></li>`;
    pagination.append(previousBtn);

    // Tạo các nút số trang
    for (let i = 1; i <= totalPage; i++) {
        let pageBtn = `<li class="page-item"><a class="page-link" href="#" onclick="changePage(${i})">${i}</a></li>`;
        pagination.append(pageBtn);
    }

    // Tạo nút "Next" nếu currentPage nhỏ hơn totalPage
    if (currentPage <= totalPage - 1) {
        let nextBtn = `<li class="page-item"><a class="page-link" href="#" onclick="changePage(${currentPage + 1})">Next</a></li>`;
        pagination.append(nextBtn);
    }

}


function changePage(pageNumber) {
    if (pageNumber === 0 && currentPage === 1) {
        return;
    }
    currentPage = pageNumber;
    product.getAllProduct(currentPage - 1, pageSize);
}

$.ajax({
    url: '/api/categorys',
    "method": "GET",
    contentType: 'application/json',
    success: function (data) {
        var categorySelect = $('#categorySelect');
        $.each(data, function (index, category) {
            // Thêm một option mới với giá trị là id và hiển thị là nameCategory
            categorySelect.append('<option value="' + category.id + '">' + category.nameCategory + '</option>');
        });
    },
    error: function () {
        console.log('Failed to load category from API.');
    }
});
//--------------------------------------- add product-----------------------------------------------------------------------------------

let addProduct = {
    productAdd: function (obj) {
        $.ajax({
            url: '/api/products',
            "method": "POST",
            contentType: 'application/json',
            "data": JSON.stringify(obj),
            success: function () {
                product.getAllProduct(currentPage - 1, pageSize);
            },
            error() {

            }
        })


    }

}
$("#btn-save").click(function () {
    event.preventDefault();
    var submit = $("#insert-product");
    if (submit == false) {

        return;
    }
    const nameProduct = $("#name-product").val();
    const priceProduct = $("#price-product").val();
    const quantity = $("#quantity").val();
    const color = $("#Color").val();
    const status = $("#Status").val();
    const category = $("#categorySelect").val();
    const obj = {nameProduct, priceProduct, quantity, color, status, category};
    try {
        addProduct.productAdd(obj);
    } catch (error) {

    }
});

