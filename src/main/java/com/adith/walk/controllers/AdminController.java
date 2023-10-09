package com.adith.walk.controllers;

import com.adith.walk.dto.*;
import com.adith.walk.entities.*;
import com.adith.walk.enums.OrderStatus;
import com.adith.walk.exceptions.BannerNotFoundException;
import com.adith.walk.exceptions.SizeAlreadyExistsException;
import com.adith.walk.exporters.OrderPDFExporter;
import com.adith.walk.helper.Message;
import com.adith.walk.mappers.EntityMapper;
import com.adith.walk.repositories.ImageRepo;
import com.adith.walk.service.BannerService;
import com.adith.walk.service.CategoryService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.coupon.service.CouponService;
import com.adith.walk.service.file.service.FileService;
import com.adith.walk.service.order.service.OrderService;
import com.adith.walk.service.orderitem.service.OrderItemService;
import com.adith.walk.service.review.service.ReviewService;
import com.adith.walk.service.stock.service.StockService;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes({"product"})
@RequestMapping("admin")
public class AdminController {

    final
    FileService fileService;
    final
    CustomerService customerService;
    final
    ProductService productService;
    final
    CouponService couponService;

    final
    BCryptPasswordEncoder bCryptPasswordEncoder;

    final
    ModelMapper modelMapper;
    final EntityMapper entityMapper;

    final
    ImageRepo imageRepo;

    final
    BannerService bannerService;

    final
    CategoryService categoryService;

    final OrderService orderService;

    final StockService stockService;

    final ReviewService reviewService;

    final OrderItemService orderItemService;

    final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(FileService fileService, CustomerService customerService, ProductService productService, CouponService couponService, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, EntityMapper entityMapper, ImageRepo imageRepo, BannerService bannerService, CategoryService categoryService, OrderService orderService, StockService stockService, ReviewService reviewService, OrderItemService orderItemService) {
        this.fileService = fileService;
        this.customerService = customerService;
        this.productService = productService;
        this.couponService = couponService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.entityMapper = entityMapper;

        this.imageRepo = imageRepo;
        this.bannerService = bannerService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.stockService = stockService;
        this.reviewService = reviewService;
        this.orderItemService = orderItemService;
    }


    @GetMapping("login")
    public String getAdminLoginPage(Authentication authentication) {

        if (authentication != null) {
            return "redirect:/admin/dashboard";
        }

        return "admin/login";
    }


    @GetMapping("dashboard")
    public String getAdminDashboard(Principal principal, HttpSession session, Model model) {

        session.setAttribute("admin", principal);
        model.addAttribute("saleToday", orderService.getSaleToday());
        model.addAttribute("orderToday", orderService.getTotalOrderToday());
        model.addAttribute("pendingReview", reviewService.getCountOfPendingReviews());
        model.addAttribute("stockOutCount", stockService.getStockOutProductCount());

        return "admin/dashboard";
    }

//-----------------------------------------USER MANAGEMENT-----------------------------------------------------------------------


    //--------------------------displaying the user-----------------------------------------------

    @GetMapping("users/page/{pageNumber}")
    public String getPageOfCustomers(@PathVariable Integer pageNumber, Model model) {


        model.addAttribute("userResponse", customerService.getPageOfCustomer(pageNumber, 10));
        return "admin/users/list";

    }

    //--------------------------adding the user-----------------------------------------------
    @GetMapping("users/add")
    public String addUser(@ModelAttribute("customer") CustomerRegistrationRequest customerRegistrationRequest, Model model) {

        return "admin/users/add";
    }


    @PostMapping("users/add")
    public String addUserPOST(@Valid @ModelAttribute("customer") CustomerRegistrationRequestAdmin registrationRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("message", new Message("Enter Valid Data", "alert-danger"));
            return "admin/users/add";
        }

        try {
            customerService.registerCustomerAdmin(registrationRequest);

        } catch (AlreadyUsedException usedException) {
            model
                    .addAttribute("message",
                            new Message(usedException.getMessage(), "alert-danger"));
            return "admin/users/add";
        }

        model
                .addAttribute("message",
                        new Message("Successfully Registered", "alert-success"));
        return "admin/users/add";

    }
    //--------------------------updating the user-----------------------------------------------


    @GetMapping("users/update/{userId}")
    public String updateUserGET(@PathVariable Long userId, Model model) {

        CustomerDTO customer =
                modelMapper
                        .map(customerService.getCustomerById(userId), CustomerDTO.class);

        model
                .addAttribute("customer", customer);

        return "admin/users/update";
    }


    @PutMapping("users/update/{userId}")
    public String updateUser(@PathVariable String userId, @Valid @ModelAttribute("customer") CustomerDTO customer, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model
                    .addAttribute("message",
                            new Message("Enter Valid Data", "alert-danger"));
            return "admin/users/update";
        }

        try {
            customerService.updateCustomer(customer);
        } catch (Exception e) {
            model
                    .addAttribute("message",
                            new Message(e.getMessage(), "alert-danger"));
            return "admin/users/update";
        }


        model
                .addAttribute("message",
                        new Message("Successfully Updated", "alert-success"));
        return "admin/users/update";
    }


    //--------------------------deleting the user-----------------------------------------------

    @DeleteMapping("users/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {

        customerService.deleteCustomerById(id);

        return "redirect:/admin/users/page/0";
    }

    @PutMapping("users/enable/{userId}")
    public String enableUser(@PathVariable Long userId) {

        customerService.enableUser(userId);

        return "redirect:/admin/users/page/0";
    }

    @PutMapping("users/disable/{userId}")
    public String disableUser(@PathVariable Integer userId) {

        customerService.disableUser(userId);

        return "redirect:/admin/users/page/0";
    }


//----------------------------------PRODUCT MANAGEMENT-----------------------------------------------------------------------


    //--------------------------displaying  the product-----------------------------------------------
    @GetMapping("products/page/{pageNumber}")
    public String getAllProducts(@PathVariable Integer pageNumber, Model model) {


        model.addAttribute("productPageResponse", productService.getPageOfProducts(pageNumber, 2));
        return "admin/products/list";

    }


    //-------------------------------adding  the product-----------------------------------------------
    @GetMapping("products/add")
    public String addProduct(ProductDTO productDTO, Model model,
                             @ModelAttribute("newCategory") ProductCategory productCategory) {

        model
                .addAttribute("product", productDTO);

        model
                .addAttribute("productCategories", categoryService.getAllCategories());


        return "admin/products/add";

    }


    @PostMapping("/products/category/add")
    public String addCategory(@ModelAttribute("newCategory") ProductCategory productCategory) {

        categoryService.save(productCategory);

        return "redirect:/admin/products/add";
    }


    @PutMapping("/products/category/update/{id}")
    public String updateCategory(@PathVariable("id") Long productId, @ModelAttribute("newCategory") ProductCategory productCategory) {

        categoryService.save(productCategory);

        return "redirect:/admin/products/update/".concat(String.valueOf(productId));
    }


    @PostMapping("products/add")
    public String addProductPost(@Valid @ModelAttribute("product") ProductDTO addProductRequest, BindingResult result, @RequestParam("files") MultipartFile[] files, @RequestParam("productCategory") Integer categoryId, Model model) {

        if (result.hasErrors()) {
            model
                    .addAttribute("productCategories", categoryService.getAllCategories());
            System.out.println("helooooooo--------------------");

            return "admin/products/add";
        }

        if (files.length > 5 || files.length < 1) {
            model
                    .addAttribute("message",
                            new Message("Maximum 4 Images are allowed", "alert-danger"));
            model

                    .addAttribute("productCategories", categoryService.getAllCategories());
            return "admin/products/add";
        }

        Product product;


        try {
            addProductRequest.setProductCategory(categoryService.getCategoryById(categoryId));
            product = productService.addProduct(addProductRequest, files);
        } catch (FileAlreadyExistsException e) {
            model
                    .addAttribute("message",
                            new Message("Change the name of the file", "alert-danger"));
            model
                    .addAttribute("productCategories", categoryService.getAllCategories());
            return "admin/products/add";

        } catch (AlreadyUsedException | IllegalArgumentException e) {
            model
                    .addAttribute("message",
                            new Message(e.getMessage(), "alert-danger"));
            model
                    .addAttribute("productCategories", categoryService.getAllCategories());
            return "admin/products/add";

        } catch (Exception e) {
            logger.error("exception{}", e.getMessage());
            model.
                    addAttribute("message",
                            new Message("Something went wrong", "alert-danger"));
            model
                    .addAttribute("productCategories", categoryService.getAllCategories());
            return "admin/products/add";
        }

        model.addAttribute("message", new Message("Successfully Uploaded", "alert-success"));
        model
                .addAttribute("product", modelMapper.map(product, ProductDTO.class));
        model

                .addAttribute("productCategories", categoryService.getAllCategories());
        return "redirect:/admin/products/" + product.getProductId() + "/stock/add";
    }


    @GetMapping("products/stock")
    public String getStock(Model model) {

        model.addAttribute("stock", stockService.getAllStock());
        return "admin/products/stock";
    }


    @PostMapping("products/stock/{stockId}/size/add")
    public String updateSize(@PathVariable Long stockId, @RequestParam short size, Model model) {


        try {
            productService.addSize(stockId, size);
        } catch (SizeAlreadyExistsException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
        }
        Integer productId = stockService.getStockById(stockId).getProduct().getProductId();

        return "redirect:/admin/products/" + productId + "/stock/add";
    }


    @GetMapping("products/{productId}/stock/add")
    public String createStock(@PathVariable Integer productId, Model model) {

        model.addAttribute("stock", productService.getProductById(productId).getStock());

        return "admin/products/addStock";
    }


    @PostMapping("products/{productId}/stock/add")
    public String saveStock(@ModelAttribute("stock") Stock stock, @PathVariable Integer productId, Model model) {

        productService.updateStock(productId, stock);

        model.addAttribute("stockMessage", new Message("Stock Updated SuccessFully", "alert-success"));

        return "admin/products/addStock";
    }

//--------------------------Updating  the product-----------------------------------------------

    @GetMapping("products/update/{productId}")
    public String getUpdateProduct(@PathVariable Integer productId, Model model,
                                   @ModelAttribute("newCategory") ProductCategory productCategory) {


        ProductDTO productUpdateRequest =
                modelMapper
                        .map(productService.getProductById(productId), ProductDTO.class);


        model
                .addAttribute("productUpdateRequest", productUpdateRequest);

        model
                .addAttribute("productCategories", categoryService.getAllCategories());

        model
                .addAttribute("stock", productUpdateRequest.getStock());
        return "admin/products/update";
    }


    @PutMapping("products/update/{productId}")
    public String updateProduct(@Valid @ModelAttribute("productUpdateRequest") ProductDTO productUpdateRequest, BindingResult result, @PathVariable Integer productId, @RequestParam("files") MultipartFile[] images, Model model) {

        if (result.hasErrors()) {

            model.addAttribute("message", new Message("Enter Valid Data", "alert-danger"));
            return "admin/products/update";
        }


        try {
            productService.updateProduct(productUpdateRequest, images);

        } catch (AlreadyUsedException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "admin/products/update";
        }


        model.addAttribute("message", new Message("Successfully Updated", "alert-success"));
        return "admin/products/update";

    }

//--------------------------deleting  the product-----------------------------------------------

    @DeleteMapping("products/delete/{productId}")
    public String deleteProduct(@PathVariable Integer productId) {

        productService.deleteProduct(productId);
        return "redirect:/admin/products/page/0";

    }

    @GetMapping("/products/reviews")
    public String getProductReviews(Model model) {

        model.addAttribute("pendingReviews", reviewService.getAllPendingReviews());

        return "admin/products/reviews";
    }


    //---------------------banner part----------------------------------------------

    @GetMapping("banners")
    public String Banners(Model model) {
        model.addAttribute("bannerList", bannerService.getAllBanner());
        return "admin/banner/banners";
    }

    @GetMapping("banners/add")
    public String getBannerPage() {
        return "admin/banner/add";
    }

    @PostMapping("banners/add")
    public String getBannerPage(@RequestParam MultipartFile imageFile,
                                @RequestParam String bannerHeading,
                                @RequestParam String bannerStatus,
                                @RequestParam String bannerPosition,
                                @RequestParam String bannerDescription
    ) {

        Banner banner = new Banner();
        banner.setBannerPosition(bannerPosition);
        banner.setStatus(bannerStatus);
        banner.setDescription(bannerDescription);
        banner.setHeading(bannerHeading);

        try {
            bannerService.createNewBanner(fileService.uploadBanner(imageFile), banner);
        } catch (MultipartException | IOException e) {
            return "admin/banner/add";
        }


        return "admin/banner/add";
    }


    @PostMapping("banners/delete/{bannerId}")
    public String deleteBanner(@PathVariable Integer bannerId, Model model) {

        try {
            bannerService.delete(bannerId);
        } catch (IOException e) {
            model.addAttribute("message", new Message("cant delete", "alert-danger"));
            return "redirect:/admin/banners";
        } catch (BannerNotFoundException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "redirect:/admin/banners";
        }
        return "redirect:/admin/banners";
    }


    //-------------order section---------------------


    @GetMapping("orders")
    public String getAllOrders(Model model) {

        model.addAttribute("orders", orderService.getAllOrders());

        return "admin/orders/all";
    }


    @ResponseBody
    @CrossOrigin(originPatterns = "*")
    @GetMapping("rest/orders")
    public ResponseEntity<List<OrderResponseEntity>> getAllOrders() {
        List<OrderResponseEntity> orders = new ArrayList<>();
        orderService.getAllOrders().forEach(order -> {
            Customer customer = new Customer();
            customer.setUserId(order.getCustomer().getUserId());
            order.setCustomer(customer);

            orders.add(modelMapper.map(order, OrderResponseEntity.class));
        });


        return ResponseEntity.ok(orders);
    }

    @GetMapping("orders/{orderId}")
    public String getAllOrders(@PathVariable Long orderId, Model model) {

        model.addAttribute("order", orderService.getOrderById(orderId));
        model.addAttribute("orderStatus", orderService.getOrderStatus(orderId));

        return "admin/orders/order";
    }

    @PostMapping("orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus, Model model) {

        orderService.updateOrderStatus(orderId, orderStatus);

        return "redirect:/admin/orders/" + orderId;
    }


    @DeleteMapping("/products/review/{reviewId}/delete")
    public String deleteProductReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return "redirect:/admin/products/reviews";
    }

    @Transactional
    @PutMapping("/products/review/{reviewId}/approve")
    public String approveProductReview(@PathVariable Long reviewId) {

        reviewService.approveReview(reviewId);

        return "redirect:/admin/products/reviews";
    }


    @GetMapping("/sales/export")
    public void exportToCSV(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate, @RequestParam("reportType") String reportType, HttpServletResponse response) throws IOException {


        List<SalesReportDto> salesReportDtoList =
                new ArrayList<>();
        orderItemService.getAllOrderItems()
                .stream()
                .filter(orderItem -> {
                    return orderItem.getOrder().getOrderDate().isAfter(startDate) &&
                            orderItem.getOrder().getOrderDate().isBefore(endDate);
                }).forEach(orderItem -> {
                    SalesReportDto salesReportDto = entityMapper.OrderItemToSalesReportDtoMapper(orderItem);
                    salesReportDtoList.add(salesReportDto);
                });


        if (reportType.equals("csv")) {
            response.setContentType("text/csv");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=sales_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);


            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            String[] csvHeader = {"id", "name", "size", "quantity", "totalPrice", "taxRate", "finalPrice"};
            String[] nameMapping = {"id", "name", "size", "quantity", "totalPrice", "taxRate", "finalPrice"};

            csvWriter.writeHeader(csvHeader);

            for (SalesReportDto item : salesReportDtoList) {
                csvWriter.write(item, nameMapping);
            }

            csvWriter.close();

        } else if (reportType.equals("pdf")) {
            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);


            OrderPDFExporter exporter = new OrderPDFExporter(salesReportDtoList);
            exporter.export(response);

        }


    }

}