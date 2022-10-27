package com.nanum.houseservice.house.presentation;

import com.nanum.config.BaseResponse;
import com.nanum.exception.ExceptionResponse;
import com.nanum.exception.NoHouseFileException;
import com.nanum.houseservice.house.application.HouseService;
import com.nanum.houseservice.house.dto.HouseDto;
import com.nanum.houseservice.house.dto.HouseSearchDto;
import com.nanum.houseservice.house.vo.*;
import com.nanum.util.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "하우스", description = "하우스 관련 api")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "201", description = "created successfully", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
})
public class HouseController {

    private final HouseService houseService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "하우스 등록 API", description = "호스트가 하우스를 등록하는 요청")
    @PostMapping("/houses")
    public ResponseEntity<Object> createHouse(@Valid @RequestPart HouseRequest houseRequest,
                                              @RequestPart(value = "houseMainImg", required = false) MultipartFile houseMainImg,
                                              @RequestPart(value = "floorPlanImg", required = false) MultipartFile floorPlanImg,
                                              @RequestPart(value = "houseFile", required = false) MultipartFile houseFile,
                                              @RequestPart(value = "houseImgs", required = false) List<MultipartFile> houseImgs) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseDto houseDto = mapper.map(houseRequest, HouseDto.class);
        StringBuilder keyWord = new StringBuilder();

        for (String s : houseRequest.getKeyWord()) {
            keyWord.append("#").append(s);
        }

        houseDto.setKeyWord(String.valueOf(keyWord));

        if (houseFile == null || houseFile.isEmpty()) {
            throw new NoHouseFileException("HouseFile Cannot Be Empty");
        }

        HouseCreateResponse response = houseService.createHouse(houseDto, houseMainImg, floorPlanImg, houseFile, houseImgs);

        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>(response));
    }

    @Operation(summary = "본인 하우스 목록 조회 API", description = "호스트가 본인 하우스 목록을 조회하는 요청")
    @GetMapping("/houses/{hostId}")
    public ResponseEntity<Object> retrieveHostAllHouses(@PathVariable("hostId") Long hostId,
                                                        Pageable pageable) {

        Page<HostHouseResponse> response = houseService.retrieveHostAllHouses(hostId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 상세 조회 API", description = "하우스 상세 정보를 조회하는 요청")
    @GetMapping("/houses/house/{houseId}")
    public ResponseEntity<Object> retrieveHouseDetails(@PathVariable("houseId") Long houseId) {

        String token = jwtProvider.customResolveToken();
        Long userId = token != null ? Long.valueOf(jwtProvider.getUserPk(token)) : null;

        HouseResponse response = houseService.retrieveHouseDetails(userId, houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "기존 하우스 정보 조회 API", description = "호스트가 하우스 수정을 위해 기존 정보를 조회하는 요청")
    @GetMapping("/houses/{hostId}/origin/{houseId}")
    public ResponseEntity<Object> retrieveOriginHouse(@PathVariable("hostId") Long hostId,
                                                      @PathVariable("houseId") Long houseId) {

        HouseOriginResponse response = houseService.retrieveOriginHouse(hostId, houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 수정 API", description = "호스트가 본인 하우스 정보 및 하우스 옵션을 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}")
    public ResponseEntity<Object> updateHouse(@PathVariable("hostId") Long hostId,
                                              @PathVariable("houseId") Long houseId,
                                              @Valid @RequestPart HouseUpdateRequest houseRequest,
                                              @RequestPart(value = "houseMainImg", required = false) MultipartFile houseMainImg,
                                              @RequestPart(value = "floorPlanImg", required = false) MultipartFile floorPlanImg) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HouseDto houseDto = mapper.map(houseRequest, HouseDto.class);
        houseDto.setHostId(hostId);

        StringBuilder keyWord = new StringBuilder();

        for (String s : houseRequest.getKeyWord()) {
            keyWord.append("#").append(s);
        }

        houseDto.setKeyWord(String.valueOf(keyWord));

        houseService.updateHouse(houseId, houseDto, houseMainImg, floorPlanImg);
        String result = "하우스 정보 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 이미지 수정 API", description = "호스트가 본인 하우스 상세 이미지를 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}/image")
    public ResponseEntity<Object> updateHouseImg(@PathVariable("hostId") Long hostId,
                                                 @PathVariable("houseId") Long houseId,
                                                 @RequestPart(required = false) List<Long> deleteHouseImgs,
                                                 @RequestPart(required = false) List<MultipartFile> houseImgs) {

        houseService.updateHouseImg(hostId, houseId, deleteHouseImgs, houseImgs);
        String result = "하우스 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 서류 수정 API", description = "호스트가 본인 하우스 서류를 수정하는 요청")
    @PutMapping("/houses/{hostId}/{houseId}/file")
    public ResponseEntity<Object> updateHouseFile(@PathVariable("hostId") Long hostId,
                                                  @PathVariable("houseId") Long houseId,
                                                  @RequestPart(required = false) MultipartFile houseFile) {

        if (houseFile == null || houseFile.isEmpty()) {
            throw new NoHouseFileException("HouseFile Cannot Be Empty");
        }

        houseService.updateHouseFile(hostId, houseId, houseFile);
        String result = "하우스 이미지 수정이 완료되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(result));
    }

    @Operation(summary = "하우스 서류 조회 API", description = "하우스 서류를 조회하는 요청")
    @GetMapping("/houses/{hostId}/{houseId}/file")
    public ResponseEntity<Object> retrieveHouseFile(@PathVariable Long hostId,
                                                    @PathVariable Long houseId) {

        HouseFileResponse response = houseService.retrieveHouseFile(hostId, houseId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 검색 API", description = "하우스를 검색하는 요청")
    @GetMapping("/houses/search")
    public ResponseEntity<Object> retrieveHouseSearch(@RequestParam(defaultValue = "") String searchWord) {

        String token = jwtProvider.customResolveToken();
        Long userId = token != null ? Long.valueOf(jwtProvider.getUserPk(token)) : null;

        List<HouseSearchResponse> response = houseService.retrieveHouseSearch(searchWord, userId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "자동완성 API", description = "하우스 검색어 추천 요청")
    @GetMapping("/houses/search/auto")
    public ResponseEntity<Object> retrieveAutoHouseSearch(@RequestParam(defaultValue = "") String searchWord) {
        List<String> response = houseService.retrieveAutoHouseSearch(searchWord);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 검색 API(feat.elastic)", description = "하우스를 검색어로 검색하는 요청")
    @GetMapping("/houses/search/elastic")
    public ResponseEntity<Object> retrieveHouseByElastic(@RequestParam(defaultValue = "", required = false) String searchWord) {
        List<HouseElasticSearchResponse> response = houseService.retrieveHouseByElastic(searchWord);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 조건 검색 API", description = "하우스를 특정 조건을 기준으로 검색하는 요청")
    @GetMapping("/houses/search/map")
    public ResponseEntity<Object> retrieveHouseByOption(@RequestParam(name = "sk", defaultValue = "", required = false) String searchWord,
                                                        @RequestParam(name = "ar", defaultValue = "", required = false) String area,
                                                        @RequestParam(name = "gt", defaultValue = "", required = false) String genderType,
                                                        @RequestParam(name = "ht", defaultValue = "", required = false) String houseType,
                                                        @RequestParam(name = "cX") Double centerX,
                                                        @RequestParam(name = "cY") Double centerY,
                                                        @RequestParam(name = "swX") Double southWestX,
                                                        @RequestParam(name = "swY") Double southWestY) {

        HouseSearchDto houseSearchDto = HouseSearchDto.builder()
                .searchWord(searchWord)
                .area(area)
                .genderType(genderType)
                .houseType(houseType)
                .centerX(centerX)
                .centerY(centerY)
                .southWestX(southWestX)
                .southWestY(southWestY)
                .build();

        List<HouseElasticSearchResponse> response = houseService.retrieveHouseByOption(houseSearchDto);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 조건 검색 API", description = "하우스를 특정 조건을 기준으로 검색하는 요청")
    @GetMapping("/houses/search/region")
    public ResponseEntity<Object> retrieveHouseByRegion(@RequestParam(name = "region") String region) {

        List<HouseElasticSearchResponse> response = houseService.retrieveHouseByRegion(region);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "지역별 하우스 개수 조회 API", description = "지역별 하우스 개수를 조회하는 요청")
    @GetMapping("/houses/search/regions")
    public ResponseEntity<Object> retrieveHouseCountByRegion() {

        List<HouseCountResponse> response = houseService.retrieveHouseCountByRegion();

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @Operation(summary = "하우스 통계 정보 조회 API", description = "하우스 방 가격, 리뷰 통계, 좋아요 통계, 좋아요 여부를 조회하는 요청")
    @GetMapping("/houses/house/{houseId}/total")
    public ResponseEntity<Object> retrieveHouseTotal(@PathVariable Long houseId) {
        String token = jwtProvider.customResolveToken();
        Long userId = token != null ? Long.valueOf(jwtProvider.getUserPk(token)) : null;

        HouseTotalResponse response = houseService.retrieveHouseTotal(houseId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(response));
    }

    @PostMapping("/houses/document")
    public ResponseEntity<Object> createHouseDocument() {
        houseService.createHouseDocument();
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse<>("하우스 문서 생성 성공"));
    }
}
