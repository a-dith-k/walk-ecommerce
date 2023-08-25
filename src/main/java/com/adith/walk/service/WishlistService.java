package com.adith.walk.service;

import com.adith.walk.Entities.Wishlist;
import com.adith.walk.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;


}
