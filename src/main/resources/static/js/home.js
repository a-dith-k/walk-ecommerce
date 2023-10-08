const search = () => {
    console.log("searching");

    let query = $("#search-input").val();

    if (query === '') {
        $("#search-result").hide()
    } else {
        console.log(query);

        //sending request to backend
        let url = `http://localhost:2021/search/${query}`;

        fetch(url).then((response) => {
            return response.json()
        }).then((data) => {

            console.log(data);
            let resultDiv = '';
            if (data.length === 0) {
                resultDiv = `<div class='m-3 p-2 fs-3'>No results found</div>`
            }


            data.forEach((product) => {
                resultDiv += `<a href='/products/${product.productId}' class='text-decoration-none  text-dark row p-2'>
                            <div class='fs-3 mt-1 ms-1  col-md-1  d-flex justify-content-end'>
                                <img src="../img/productImages/${product.list[0].name}" class="img-fluid w-50">
                            </div>
                                    <div class='d-flex align-items-center mt-1 fs-3 col-md-6'>
                                    ${product.productName}
                            </div></a>
                            `
            })


            $('#search-result').html(resultDiv);

        });
        $("#search-result").show()
    }
};


let pageNumber = 0;
let totalPages = 0;

window.onload = function () {
    request();


};

function loadPage(i) {
    pageNumber = i;
    request();
}

function previousPage() {
    if (pageNumber > 0) {
        pageNumber--;
        request();
    }

}

let numberOfProducts = 5;

function productCount(count) {

    numberOfProducts = count;
    request();
}


function nextPage() {
    if (pageNumber < totalPages - 1) {
        pageNumber++;
        request();
    }


}


function request() {

    let url = `http://localhost:2021/rest/${pageNumber}/${numberOfProducts}`;
    fetch(url)
        .then(res => {
            return res.json()

        })
        .then(data => {
            totalPages = data.totalPages;
            console.log(data)

            let result = '';

            data.products.content.forEach(product => {

                result +=
                    `<div class="col-sm-12 col-md-3 product position-relative" >
                    <div class="product-img">
                    <a href="/products/${product.productId}"><img
                               src="../img/productImages/${product.list[0].name}" alt=""
                                class="img-fluid img-thumbnail  ">
                    </a>
                </div>

                <div class="details-product position-absolute top-0 d-flex row w-100 p-5 m-0">
                    <div class="col-6 d-flex  flex-column align-items-start ">
                            <h1 
                                class="text-dark fw-light fs-4">${product.productName}</h1>
                            <h1 
                                class="text-secondary fw-bold fs-3 ">${product.brand}</h1>
                    </div>
                    <div class="col-6 d-flex justify-content-center flex-column align-items-center">
                            <h1 
                                class="display-6 text-decoration-line-through text-secondary">&#8377 ${product.productMrp}</h1>
                            <h1 
                                class="display-5 fs-1 fst-italic border-black m-1"> &#8377 ${product.offerPrice}</h1>
                    </div>
                </div>
           </div>`

            })

            let pages = '';

            for (let i = data.currentPageNumber - 2; i < data.currentPageNumber + 5; i++) {
                let active = '';
                if (i == data.currentPageNumber) {
                    active = "active";
                }

                if (i < totalPages && i >= 0) {
                    pages += `      <li class="page-item"  >
                            <button  class="page-link ${active}"    onclick="loadPage(${i})" ><span>${i + 1}</span></button>
       
                    </li>`;
                }
            }

            pageNumber = data.currentPageNumber;
            let pagination = `<nav aria-label="...">
                <ul class="pagination" >
                    <li class="page-item " >
                      <button class="page-link"  onclick="previousPage()" tabindex="-1" aria-disabled="true">Previous</button>
                    </li>
                    
                   ${pages}
              
                    <li class="page-item">
                        <button class="page-link"  onclick="nextPage()">Next</button>
                    </li>
                </ul>
            </nav>`


            let productCount = `<div class="w-50">
                    
                
                   <button class="btn btn-primary fs-2"  onclick="productCount(2)" >2</button>
                   <button class="btn btn-primary fs-2"  onclick="productCount(3)" >3</button>
                   <button class="btn btn-primary fs-2"  onclick="productCount(4)" >4</button>
                    

               
            </div>`


            $("#productCount").html(productCount);

            $("#paginationDiv").html(pagination);
            $("#productList").html(result);


        })

}






