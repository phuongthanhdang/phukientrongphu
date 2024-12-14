
        // Function to fetch and display categories from API
//       const URI = `https://phukientrongphu-1.onrender.com/api/categories`;
        const URI = `http://localhost:8080/api/categories`;
        async function loadCategories() {
            const container = document.getElementById('category-tr');
            container.innerHTML = '<p>Loading categories...</p>'; // Show loading text

            try {
                // Fetch data from API
                const response = await fetch(URI);
                if (!response.ok) {
                    throw new Error('Failed to fetch categories');
                }
                const categories = await response.json();

                // Clear loading text
                container.innerHTML = '';

                // Display categories dynamically
                categories.forEach((category, index) => {
                    const categoryCard = `
                                            <tr>
                                               <td>${index + 1}</td>
                                               <td><img src=../${category.imageUrl} alt="Category Image" class="img-thumbnail" style="width: 50px;"></td>
                                               <td>${category.name}</td>
                                               <td>
                                                   <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#editCategoryModal">Edit</button>
                                                   <button class="btn btn-danger btn-sm" onclick="deleteCategory(${category.id})">Delete</button>
                                               </td>
                                           </tr>`;
                    container.innerHTML += categoryCard;
                });
            } catch (error) {
                console.error('Error loading categories:', error);
                container.innerHTML = '<p class="text-danger">Failed to load categories. Please try again later.</p>';
            }
        }

        // Call loadCategories when the page is loaded
        window.onload = loadCategories;




//add category
const CATEGORY_API = 'http://localhost:8080/api/categories';
    const categoryForm = document.getElementById('categoryForm');

    categoryForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData();
        formData.append('name', document.getElementById('categoryName').value);
        formData.append('image', document.getElementById('categoryImage').files[0]);

        fetch(CATEGORY_API, {
            method: 'POST',
            body: formData,
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to create category');
            return response.text();
        })
        .then(() => {
            alert('Category created successfully!');
            window.location.reload();
        })
        .catch(error => {
            console.error('Error creating category:', error);
            alert('Failed to create category!');
        });
    });

// API xóa category
async function deleteCategory(categoryId) {
    try {
        const response = await fetch(`http://localhost:8080/api/categories/${categoryId}`, {
            method: 'DELETE',
        });
        if (response.ok) {
            alert("Category deleted successfully");
            loadCategories();
            loadProducts();
            // Cập nhật giao diện hoặc load lại danh sách category
        } else {
            alert("Error deleting category");
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

