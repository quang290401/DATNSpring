
//get data after redirect from view list
var url = document.location.pathname;
var id = url.substring(url.lastIndexOf('/') + 1);
var productEdit2 = {
    getProductById: function (id) {
        $.ajax({
            url: `/api/products/` + id,
            method: "GET",
            contentType: 'application/json',
            success: function (response, textStatus, xhr) {
                displayProductEdit(response);
            },
            error: function (error) {
                alert("fail");
            }
        });
    }
};

productEdit2.getProductById(id);



function displayProductEdit(data) {
    if (data && typeof data === 'object') {
        $("#id-update").val(data.id);
        $("#update-name-product").val(data.nameProduct);
        $("#update-price-product").val(data.priceProduct);
        $("#update-quantity").val(data.quantity);
        $("#update-Color").val(data.color);
        $("#update-Status").val(data.status);
        $("#category-id").val(data.category);

    }
}


let updateProduct = {
    productUpdate: function (obj) {
        $.ajax({
            url: '/api/products' ,
            method: "PUT",
            contentType: 'application/json',
            data: JSON.stringify(obj), // Truyền đối tượng sản phẩm vào data
            success: function () {
                window.location.href = '/product'
            },
            error: function() {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
};
$("#btn-update").click(function (event) {
    event.preventDefault();
    var isValid = $("#update-product").valid(); // Kiểm tra tính hợp lệ của biểu mẫu
    if (!isValid) {
        return;
    }
    const nameProduct = $("#update-name-product").val();
    const priceProduct = $("#update-price-product").val();
    const quantity = $("#update-quantity").val();
    const color = $("#update-Color").val();
    const status = $("#update-Status").val();
    const id = $("#id-update").val();
    const obj = { nameProduct, priceProduct, quantity, color, status, id };

    updateProduct.productUpdate(obj);
});


