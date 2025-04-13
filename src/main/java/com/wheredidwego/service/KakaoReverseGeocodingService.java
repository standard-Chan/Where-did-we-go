package com.wheredidwego.service;

import com.wheredidwego.dto.RegionInfoDto;
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

    private final RestTemplate restTemplate = new RestTemplate();

    public RegionInfoDto getRegionFromCoords(double lat, double lng) {
        // url 설정
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json"
                + "?x=" + lng + "&y=" + lat;
        System.out.println(url);

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
                String district = region.getString("region_2depth_name"); // 읍,면,동

                return new RegionInfoDto(province, district);
            } else {
                throw new GeocodingException("좌표에 해당하는 지역 정보를 찾을 수 없습니다.");
            }
        } catch (HttpClientErrorException e) {
            throw new GeocodingException("카카오 API 요청 오류: " + e.getMessage());
        } catch (JSONException e) {
            throw new GeocodingException("응답 데이터 파싱 오류: " + e.getMessage());
        } catch (Exception e) {
            throw new GeocodingException("역지오코딩 처리 중 알 수 없는 오류가 발생했습니다.");
        }
    }

}


