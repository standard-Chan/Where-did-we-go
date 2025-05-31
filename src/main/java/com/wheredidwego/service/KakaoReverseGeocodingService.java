package com.wheredidwego.service;

import com.wheredidwego.dto.region.RegionInfoDto;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.GeocodingException;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Transactional
@Service
public class KakaoReverseGeocodingService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    private String KAKAO_REVERGEOCODING_API = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    private final RestTemplate restTemplate = new RestTemplate();

    public RegionInfoDto getRegionFromCoords(double lat, double lng) {
        if (lat < 33 || lat > 39 || 124 > lng || lng > 132) {
            throw new GeocodingException(ErrorCode.OUT_OF_KOREA);
        }
        // url 설정
        String url = KAKAO_REVERGEOCODING_API
                + "?x=" + lng + "&y=" + lat;

        // header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        try {

            JSONObject json = new JSONObject(responseBody);
            JSONArray documents = json.getJSONArray("documents");

            if (documents.length() > 0) {
                // get : 법정동 data
                JSONObject region = documents.getJSONObject(0);
                String province = region.getString("region_1depth_name"); // 도
                String district = region.getString("region_2depth_name"); // 시, 구
                String subdistrict = region.getString("region_3depth_name"); // 읍면동

                return new RegionInfoDto(province, district, subdistrict);
            } else {
                throw new GeocodingException(ErrorCode.LOCATION_NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            throw new GeocodingException(ErrorCode.KAKAO_API_ERROR);
        } catch (JSONException e) {
            throw new GeocodingException(ErrorCode.PARSING_ERROR);
        } catch (Exception e) {
            throw new GeocodingException(ErrorCode.UNKNOWN_ERROR);
        }
    }

}


