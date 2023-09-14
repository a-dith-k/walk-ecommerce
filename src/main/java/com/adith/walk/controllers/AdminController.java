package com.adith.walk.controllers;

import com.adith.walk.Entities.*;
import com.adith.walk.dto.CustomerDTO;
import com.adith.walk.dto.CustomerRegistrationRequest;
import com.adith.walk.dto.CustomerRegistrationRequestAdmin;
import com.adith.walk.dto.ProductDTO;
import com.adith.walk.enums.OrderStatus;
import com.adith.walk.helper.Message;
import com.adith.walk.repositories.ImageRepo;
import com.adith.walk.service.*;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes("product")
@RequestMapping("admin")
public class AdminController {

    final
    FileService fileService;
    final
    CustomerService  customerService;
    final
    ProductService productService;
    final
    CouponService couponService;

    final
    BCryptPasswordEncoder bCryptPasswordEncoder;

    final
    ModelMapper modelMapper;

    final
    ImageRepo imageRepo;

    final
    BannerService bannerService;


    final
    CategoryService categoryService;

    final OrderService orderService;
    final StockService stockService;

    public AdminController(FileService fileService, CustomerService customerService, ProductService productService, CouponService couponService, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, ImageRepo imageRepo, BannerService bannerService, CategoryService categoryService, OrderService orderService, StockService stockService) {
        this.fileService = fileService;
        this.customerService = customerService;
        this.productService = productService;
        this.couponService = couponService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.imageRepo = imageRepo;
        this.bannerService = bannerService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.stockService = stockService;
    }



    @GetMapping("login")
    public String getAdminLoginPage(Authentication authentication){

        if(authentication!=null){
            return  "redirect:/admin/dashboard";
        }

        return "admin/login";
    }


    @GetMapping("dashboard")
    public String getAdminDashboard(Principal principal,HttpSession session){

        session.setAttribute("admin",principal);

        return "admin/dashboard" ;
    }

//-----------------------------------------USER MANAGEMENT-----------------------------------------------------------------------


    //--------------------------displaying the user-----------------------------------------------

    @GetMapping("users/page/{pageNumber}")
    public String getPageOfCustomers(@PathVariable Integer pageNumber, Model model){


        model.addAttribute("userResponse",customerService.getPageOfCustomer(pageNumber,10    ));
        return "admin/users/list";

    }

    //--------------------------adding the user-----------------------------------------------
    @GetMapping("users/add")
    public String  addUser(@ModelAttribute("customer") CustomerRegistrationRequest customerRegistrationRequest, Model model ){

        return  "admin/users/add";
    }



    @PostMapping("users/add")
    public String  addUserPOST(@Valid @ModelAttribute("customer") CustomerRegistrationRequestAdmin registrationRequest, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("message",new Message("Enter Valid Data","alert-danger"));
            return "admin/users/add";
        }

        try{
                customerService.registerCustomerAdmin(registrationRequest);

        }catch (AlreadyUsedException usedException){
                model
                        .addAttribute("message",
                                new Message(usedException.getMessage(),"alert-danger"));
                return  "admin/users/add";
        }

        model
                .addAttribute("message",
                        new Message("Successfully Registered","alert-success"));
        return  "admin/users/add";

    }
    //--------------------------updating the user-----------------------------------------------



    @GetMapping("users/update/{userId}")
    public String updateUserGET(@PathVariable Long userId,Model model){

        CustomerDTO customer =
                    modelMapper
                        .map(customerService.getCustomerById(userId), CustomerDTO.class);

        model
                .addAttribute("customer",customer);

        return "admin/users/update";
    }



    @PutMapping("users/update/{userId}")
    public String updateUser(@PathVariable String userId , @Valid @ModelAttribute("customer") CustomerDTO customer, BindingResult result, Model model){

        if(result.hasErrors()){
            model
                    .addAttribute("message",
                            new Message("Enter Valid Data","alert-danger"));
            return "admin/users/update";
        }

        try{
            customerService.updateCustomer(customer);
        }catch (Exception e){
            model
                    .addAttribute("message",
                            new Message(e.getMessage(),"alert-danger"));
            return "admin/users/update";
        }


        model
                .addAttribute("message",
                        new Message("Successfully Updated","alert-success"));
        return "admin/users/update";
    }


    //--------------------------deleting the user-----------------------------------------------

    @DeleteMapping("users/delete/{id}")
    public String deleteBook(@PathVariable Integer id ){

        customerService.deleteCustomerById(id);

        return "redirect:/admin/users/page/0";
    }

    @PutMapping("users/enable/{userId}")
    public String enableUser(@PathVariable Long userId){

        customerService.enableUser(userId);

        return "redirect:/admin/users/page/0";
    }

    @PutMapping("users/disable/{userId}")
    public String disableUser(@PathVariable Integer userId){

        customerService.disableUser(userId);

        return "redirect:/admin/users/page/0";
    }




//----------------------------------PRODUCT MANAGEMENT-----------------------------------------------------------------------



    //--------------------------displaying  the product-----------------------------------------------
    @GetMapping("products/page/{pageNumber}")
    public String getAllProducts(@PathVariable Integer pageNumber, Model model){


        model.addAttribute("productPageResponse",productService.getPageOfProducts(pageNumber,2));
        return "admin/products/list";

    }


    //-------------------------------adding  the product-----------------------------------------------
    @GetMapping("products/add")
    public String addProduct(ProductDTO productDTO, Model model){

        model
                .addAttribute("product",productDTO);

        return "admin/products/add";

    }

//    @PostMapping("products/add/stock/size/add")
//    public String addNewSize(@RequestParam short size,@ModelAttribute ProductDTO productAddRequestDto,Model model,HttpSession session){
//
//
//        System.out.println(productAddRequestDto.getProductId());
//        System.out.println(productAddRequestDto.getStock().getStockId());
//        Size newSize=new Size();
//        newSize.setSizeNumber(size);
//        newSize.setSizeLength((short) 0);
//        newSize.setSizeWidth((short) 0);
//        newSize.setTotalCount(0);
//        System.out.println(productAddRequestDto.getStock());
//
//        if(productAddRequestDto.getStock()==null){
//            Stock stock=new Stock();
//            productAddRequestDto.setStock(stock);
//            System.out.println("called me");
//        }
//
//
//        Stock stock = productAddRequestDto.getStock();
//        stock.getSizeList().add(newSize);
//        stock.getSizeList().forEach(size1-> System.out.println(size1.getSizeNumber()));
//        Stock save = stockService.save(stock);
//        productAddRequestDto.setStock(save);
//
//
//
//
//        model
//                .addAttribute("product",productAddRequestDto);
//
//        return "/admin/products/add";
//
//    }


    @PostMapping("products/add")
    public String addProductPost(@Valid @ModelAttribute("product") ProductDTO addProductRequest,BindingResult result,Category category, @RequestParam("files")MultipartFile[] files, Model model){

        if(result.hasErrors()){

            return "admin/products/add";
        }

        if(files.length>5||files.length<1){
            model
                    .addAttribute("message",
                            new Message("Maximum 4 Images are allowed","alert-danger"));
            return "admin/products/add";
        }

        Product product=null;




        try {
           product = productService.addProduct(addProductRequest, files);
        }catch (FileAlreadyExistsException e){
            model
                    .addAttribute("message",
                            new Message("Change the name of the file","alert-danger"));

            return "admin/products/add";

        }catch (AlreadyUsedException | IllegalArgumentException e) {
            model
                    .addAttribute("message",
                            new Message(e.getMessage(),"alert-danger"));

            return "admin/products/add";

        } catch (Exception e){
            model.
                    addAttribute("message",
                            new Message("Something went wrong","alert-danger"));

            return "admin/products/add";
        }

        model.addAttribute("message",new Message("Successfully Uploaded","alert-success"));
        model
                .addAttribute("product",modelMapper.map(product,ProductDTO.class));
        return "admin/products/addStock";
    }


    @GetMapping("products/addStock")
    public String addStock(){
        return "admin/products/addStock";
    }


    @PostMapping("products/{productId}/stock/size/add")
    public String addSize(@PathVariable Integer productId,@RequestParam String size,Model model){

        Product product = productService.getProductById(productId);
        ProductService.getStockOfProduct(product,size);
        productService.saveProduct(product);

        model.addAttribute("product",modelMapper.map(productService.getProductById(productId),ProductDTO.class));


        return "admin/products/addStock";
    }

//--------------------------Updating  the product-----------------------------------------------

    @GetMapping("products/update/{productId}")
    public String getUpdateProduct(@PathVariable Integer productId,Model model){



        ProductDTO productUpdateRequest =
                modelMapper
                        .map(productService.getProductById(productId),ProductDTO.class);


        model
                .addAttribute("productUpdateRequest",productUpdateRequest);

        model
                .addAttribute("stock",productUpdateRequest.getStock());
        return "admin/products/update";
    }


    @PutMapping("products/update/{productId}")
    public String updateProduct(@Valid @ModelAttribute("productUpdateRequest")ProductDTO productUpdateRequest,BindingResult result,@PathVariable Integer productId, @RequestParam("files")MultipartFile[] images,Model model){

        if(result.hasErrors()){

            model.addAttribute("message",new Message("Enter Valid Data","alert-danger"));
            return "admin/products/update";
        }



        try {
            productService.updateProduct(productUpdateRequest,images);

        } catch (AlreadyUsedException e) {
            model.addAttribute("message", new Message(e.getMessage(), "alert-danger"));
            return "admin/products/update";
        }


        model.addAttribute("message",new Message("Successfully Updated","alert-success"));
        return "admin/products/update";

    }

//--------------------------deleting  the product-----------------------------------------------

    @DeleteMapping("products/delete/{productId}")
    public String deleteProduct(@PathVariable Integer productId){

        productService.deleteProduct(productId);
        return "redirect:/admin/products/page/0";

    }





    //---------------------banner part----------------------------------------------

    @GetMapping("banners")
    public String Banners(Model model){
        model.addAttribute("bannerList",bannerService.getAllBanner());
        return "admin/banner/banners";
    }

    @GetMapping("banners/add")
    public String getBannerPage(){
        return "admin/banner/add";
    }

    @PostMapping("banners/add")
    public String getBannerPage(@RequestParam MultipartFile file,@RequestParam String heading,@RequestParam String description ,@RequestParam String status,Model model){


        if(file.isEmpty()){
            model.addAttribute("message","image can not be empty");
            return "admin/banner/add";
        }

        if(heading.isEmpty()||description.isEmpty()){
            model.addAttribute("message","heading and description can not be empty");
            return "admin/banner/add";
        }

        try{
                bannerService.save(new Banner(fileService.uploadBanner(file),heading,description,status));
            }catch (FileAlreadyExistsException e){
                model.addAttribute("message","file exists");
                return "admin/banner/add";
            }catch (MultipartException | IOException e){
                model.addAttribute("message",e.getMessage());
                return "admin/banner/add";
            }


        model.addAttribute("message","uploaded successfully");
        return "admin/banner/add";
    }

    @PostMapping("banners/delete/{bannerId}")
    public String deleteBanner(@PathVariable Integer bannerId,Model model){

        try {
            bannerService.delete(bannerId);
        } catch (IOException e) {
            model.addAttribute("message", new Message("cant delete", "alert-danger"));
            return "redirect:/admin/banners";
        }
        return "redirect:/admin/banners";
    }


    //-------------order section---------------------


    @GetMapping("orders")
    public String getAllOrders(Model model){

        model.addAttribute("orders",orderService.getAllOrders());

        return "admin/orders/all";
    }

    @GetMapping("orders/{orderId}")
    public String getAllOrders(@PathVariable Long orderId, Model model){

        model.addAttribute("order",orderService.getOrderById(orderId));
        model.addAttribute("orderStatus",orderService.getOrderStatus(orderId));

        return "admin/orders/order";
    }

    @PostMapping ("orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus orderStatus, Model model){

        orderService.updateOrderStatus(orderId,orderStatus);

        return "redirect:/admin/orders/"+orderId;
    }


    @GetMapping("products/stock")
    public String getStock(Model model){

        model.addAttribute("stock",stockService.getAllStock());
        return "admin/products/stock";
    }



   @PostMapping("products/{productId}/stock/size/update")
   public String updateSize(@PathVariable Integer productId,@RequestParam String size){


       productService.addStock(productId,size);

       return "redirect:/admin/products/update/"+productId;
   }



    @ResponseBody
    @GetMapping("coupons")
    public List<Coupon> getAllCoupons(){

        return couponService.getAllCoupons();
    }


    @GetMapping("add-coupons")
    public String addCoupons(){

        return "admin/coupon/add";

    }



}

