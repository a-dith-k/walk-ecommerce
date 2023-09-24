package com.adith.walk.service;

import com.adith.walk.Entities.Size;
import org.springframework.stereotype.Service;

@Service
public interface SizeService {

    Size getSizeBySizeId(long sizeId);

    void removeStock(long sizeId,long count);

    void addStock(long sizeId,long count);

}
