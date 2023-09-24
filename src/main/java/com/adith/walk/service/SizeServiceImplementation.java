package com.adith.walk.service;

import com.adith.walk.Entities.Size;
import com.adith.walk.repositories.SizeRepository;
import org.springframework.stereotype.Service;

@Service
public class SizeServiceImplementation implements  SizeService{

    final SizeRepository sizeRepository;

    public SizeServiceImplementation(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    @Override
    public Size getSizeBySizeId(long sizeId) {
        return sizeRepository.findById(sizeId).orElse(new Size());
    }

    @Override
    public void removeStock(long sizeId,long count ){
            sizeRepository.removeStock(sizeId,count);
    }

    @Override
    public void addStock(long sizeId, long count) {
        sizeRepository.addStock(sizeId,count);
    }
}
