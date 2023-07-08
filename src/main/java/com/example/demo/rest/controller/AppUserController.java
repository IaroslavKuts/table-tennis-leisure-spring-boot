package com.example.demo.rest.controller;

import com.example.demo.DTO.OrdersBulkDTO;
import com.example.demo.domain.entity.*;
import com.example.demo.domain.entity.AppOrder;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrdersService;
import com.example.demo.util.LinksAssistant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RepositoryRestController
//@RequestMapping("/users/{id}") doesn't work. https://github.com/spring-projects/spring-data-rest/issues/2087
//                                             https://stackoverflow.com/questions/69825704/spring-data-rest-controller-must-not-use-requestmapping-on-class-level-as-this
public class AppUserController {
    private static Logger LOG = LoggerFactory.getLogger(AppUserController.class);
    private final String PATH = "/users/";

    private final OrderRepository orderRepo;
    private  final OrdersService ordersService;
    private final AppUserRepository appUserRepository;
    private final LinksAssistant links;



    @Transactional
    @PostMapping(PATH + "{id}/appOrders") // full path must be entered. See https://github.com/spring-projects/spring-data-rest/issues/2087
    public ResponseEntity<?> addOrderToUser(@PathVariable("id") AppUser appUser, @RequestBody OrdersBulkDTO ordersBulkDTO) {


        orderRepo.saveAllAndFlush(ordersService.createListOfOrders(ordersBulkDTO,appUser));
        return  new ResponseEntity<>(OK);
//        return new ResponseEntity<>(EntityModel.of(savedAppOrder, links.getOrderLinks(savedAppOrder)),CREATED);
    }
    @DeleteMapping(PATH+"{id}/appOrders/{order_id}")
    public ResponseEntity<?> deleteOrderOfUser(@PathVariable("order_id") AppOrder appOrder){
        orderRepo.deleteById(appOrder.getOrder_id());

        return new ResponseEntity<>(OK);
    }
    @DeleteMapping(PATH+"{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long pathUserId, Authentication auth){

        //@AuthenticationPrincipal AppUser <principal> -  doesn't seem to work properly. It isn't populated. it's fields are null/0 etc
        //System.out.println(appUser.getUser_id() + " appUser");


        //Principal <principal2> - principal2.getName() returns email of logged in user
        //System.out.println(principal2.getName() + " principal2");

        //SecurityContextHolder... works kind of properly.
//        AppUser u = ((AppUser) SecurityContextHolder.getContext()
//                        .getAuthentication()
//                        .getPrincipal());

        //Authentication auth - seems to work properly. It looks like it uses getAuthentication() from SecurityContextHolder
        //System.out.println(((AppUser)auth.getPrincipal()).getUser_id() + "Auth -> principal ->  getUser_id");


        // There might be an issue with obtaining a collection(if present) because of fetch.lazy


        Long loggedInUserId = ((AppUser)auth.getPrincipal()).getUser_id();
        if(pathUserId != loggedInUserId) {

            return new ResponseEntity<>(BAD_REQUEST);
        }
        appUserRepository.deleteById(loggedInUserId);
        return new ResponseEntity<>(OK);
    }


}
