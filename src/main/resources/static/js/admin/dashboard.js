window.addEventListener('load', () => {


    const url = "/admin/rest/orders";


    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            // throw new Error(response.statusText);c
            console.error("Server error")
        } else {
            return response.json();
        }
    }).then(data => {

        displayOrders(data)
        data.forEach(da => {
            console.log(da.items)
        })
    }).catch(err => {
        console.error(err);
    })


    function displayOrders(data) {
        let orderData = "";
        data.forEach(order => {


            order.items.forEach(item => {

                orderData += `
                         <tr>
                         <td> <img style="width: 50px" src="/img/productImages/${item.product.list[0].name}" alt="">${item.product.productName}</td>
                           
                            <td>${item.quantity}</td>
                             <td>${item.totalPrice}</td>
                            <td>${item.product.taxRate + '%'}</td>
                          
                            <td>${item.totalPrice + (item.product.taxRate / 100) * item.totalPrice}</td>
                           
                        </tr>
               `

            })


        })

        let orderTable = document.getElementById('orderTable');
        orderTable.innerHTML = orderData.toString();
    }


    // for (let data of orders) {
    //     orderData = ` <thead>
    //         <tr>
    //             <th>OrderID</th>
    //             <th>Amount</th>
    //             <th>Discount</th>
    //             <th>Final Payment</th>
    //         </tr>
    //         </thead>
    //         <tbody>
    //         <tr>
    //             <td>$</td>
    //             <td>$23233</td>
    //             <td>$33</td>
    //             <td>$23200</td>
    //         </tr>
    //         <div>
    //             <table id="item-table" class="table table-bordered">
    //                    <thead>
    //                         <tr>
    //                             <th></th>
    //                             <th>ItemName</th>
    //                             <th>ItemPrice</th>
    //                             <th>Quantity</th>
    //                             <th>TotalPrice</th>
    //                         </tr>
    //                    </thead>
    //                    <tbody>
    //                         <tr>
    //                             <td>ItemId</td>
    //                             <td>ItemName</td>
    //                             <td>ItemPrice</td>
    //                             <td>Quantity</td>
    //                             <td>TotalPrice</td>
    //                         </tr>
    //                    </tbody>
    //             </table>
    //         </div>
    //         </tbody>`;
    //
    // }


});
