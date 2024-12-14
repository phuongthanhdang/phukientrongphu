
    const CATEGORYS_API = 'http://localhost:8080/api/categories';
    const PRODUCTS_API = 'http://localhost:8080/api/products';
    const productForm = document.getElementById('productForm');
    const categorySelect = document.getElementById('categoryId');

    function loadCategoryProducts() {
        fetch(CATEGORYS_API)
            .then(response => response.json())
            .then(categories => {
                categorySelect.innerHTML = ''; // Clear old options
                categories.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.textContent = category.name;
                    categorySelect.appendChild(option);
                });
            })
            .catch(error => console.error('Error loading categories:', error));
    }
    loadCategoryProducts();
    
    const URI_GET_PRODUCT = `http://localhost:8080/api/products`;
    async function loadProducts() {
        const container = document.getElementById('product-tr');
        container.innerHTML = '<p>Loading product...</p>'; // Show loading text

        try {
            // Fetch data from API
            const response = await fetch(URI_GET_PRODUCT);
            if (!response.ok) {
                throw new Error('Failed to fetch products');
            }
            const products = await response.json();

            // Clear loading text
            container.innerHTML = '';

            // Display categories dynamically
            products.forEach((product, index) => {
                const productCard = `
                                        <tr>
                                           <td>${index + 1}</td>
                                           <td><img src=../${product.productImage} alt="Product Image" class="img-thumbnail" style="width: 50px;"></td>
                                           <td>${product.productName}</td>
                                           <td>${product.productPrice}</td>

                                           <td>
                                               <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#editCategoryModal">Edit</button>
                                               <button class="btn btn-danger btn-sm" onclick="deleteProduct(${product.id})">Delete</button>
                                           </td>
                                       </tr>`;
                container.innerHTML += productCard;
            });
        } catch (error) {
            console.error('Error loading products:', error);
            container.innerHTML = '<p class="text-danger">Failed to load products. Please try again later.</p>';
        }
    }
    loadProducts();


    // add product
    function createProduct(){
         const productFormId = document.getElementById('productForm');
            productForm.addEventListener('submit', function (event) {
                event.preventDefault();

                const formData = new FormData();
                formData.append('productName', document.getElementById('productName').value);
                formData.append('productPrice', document.getElementById('productPrice').value);
                formData.append('productImage', document.getElementById('productImage').files[0]);
                formData.append('categoryId', document.getElementById('categoryId').value);

                fetch(PRODUCTS_API, {
                    method: 'POST',
                    body: formData,
                })
                .then(response => {
                    if (!response.ok) throw new Error('Failed to create product');
                    return response.text();
                })
                .then(() => {
                    alert('Product created successfully!');
                    document.getElementById('productForm').reset();
                    loadProducts();

                })
                .catch(error => {
                    console.error('Error creating product:', error);
                    alert('Failed to create product!');
                });
            });
    }
    createProduct();

//delele product
// API xóa product
async function deleteProduct(productId) {
    try {
        const response = await fetch(`http://localhost:8080/api/products/${productId}`, {
            method: 'DELETE',
        });
        if (response.ok) {
            alert("Product deleted successfully");
            // Cập nhật giao diện hoặc load lại danh sách product
        } else {
            alert("Error deleting product");
            loadProducts();
        }
    } catch (error) {
        console.error("Error:", error);
    }
}


