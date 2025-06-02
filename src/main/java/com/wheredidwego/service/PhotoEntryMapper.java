package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.dto.photoEntry.PhotoEntryResponseDto;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.temp.PhotoEntryWithRegionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoEntryMapper {

    private final S3Service s3Service;
    private final PhotoEntryRepository photoEntryRepository;


    public PhotoEntryResponseDto mapToDto(PhotoEntry photoEntry) {
        return new PhotoEntryResponseDto(photoEntry, s3Service);
    }

    /**
     * photoEntry list를 responseDTO list로 변환
     * @param photoEntries
     * @return
     */
    public List<PhotoEntryResponseDto> mapToDtoList(List<PhotoEntry> photoEntries) {


        List<PhotoEntryResponseDto> photoEntryDtoList;
        Long start = System.currentTimeMillis();
        //병렬처리
        photoEntryDtoList = photoEntries
                .parallelStream()
                .map(photoEntry -> {
                    PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);
                    return responseDto;
                }).toList();
        Long end = System.currentTimeMillis();

        System.out.println("dto 병렬 mapping 처리 시간(ms)1 : " + (end - start));

        return photoEntryDtoList;
    }

    /**
     * PhotoEntryWithRegionDto list를 responseDTO list로 변환.
     * @param photoEntries
     * @return
     */
    public List<PhotoEntryResponseDto> photoEntryWithRegionMapToDtoList(List<PhotoEntryWithRegionDto> photoEntries) {

        List<PhotoEntryResponseDto> photoEntryDtoList;

        Long start = System.currentTimeMillis();
        //병렬처리
        photoEntryDtoList = photoEntries
                .parallelStream()
                .map(photoEntry -> {
                    PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);
                    return responseDto;
                }).toList();

        Long end = System.currentTimeMillis();

        System.out.println("dto 병렬 mapping 처리 시간(ms)2 : " + (end - start));

        return photoEntryDtoList;
    }

    /**
     * photoEntry list를 Region 정보만 담은 responseDTO list로 변환. 권한에 따른 제한된 정보를 보여주기 위함.
     * @param photoEntries
     * @return
     */
    public List<PhotoEntryResponseDto> mapToDtoListOnlyRegion(List<PhotoEntry> photoEntries) {

        List<PhotoEntryResponseDto> photoEntryDtoList;
        Long start = System.currentTimeMillis();
        //병렬처리
        photoEntryDtoList = photoEntries
                .parallelStream()
                .map(photoEntry -> {
                    PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto();
                    responseDto.setPhotoEntryResponseDtoForRegion(photoEntry);
                    responseDto.setPhotoUrl("false");
                    return responseDto;
                }).toList();

        return photoEntryDtoList;
    }

}
