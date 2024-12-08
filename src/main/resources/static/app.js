
        // Function to fetch and display categories from API
        async function loadCategories() {
            const container = document.getElementById('category-container');
            container.innerHTML = '<p>Loading categories...</p>'; // Show loading text

            try {
                // Fetch data from API
                const response = await fetch('http://localhost:8080/api/categories');
                if (!response.ok) {
                    throw new Error('Failed to fetch categories');
                }
                const categories = await response.json();

                // Clear loading text
                container.innerHTML = '';

                // Display categories dynamically
                categories.forEach(category => {
                    const categoryCard = `
                        <div class="col-6 col-md-4">
                                    <div class="card category-card shadow-sm border-0">
                                        <img src="../../upload/OIP.jfif" class="card-img-top"
                                             alt="Electronics">
                                        <div class="card-body text-center">
                                            <h5 class="card-title">${category.name}</h5>
                                            <a href="#" class="btn btn-primary">Shop Now</a>
                                        </div>
                                    </div>
                                </div>`;
                    container.innerHTML += categoryCard;
                });
            } catch (error) {
                console.error('Error loading categories:', error);
                container.innerHTML = '<p class="text-danger">Failed to load categories. Please try again later.</p>';
            }
        }

        // Call loadCategories when the page is loaded
        window.onload = loadCategories;
