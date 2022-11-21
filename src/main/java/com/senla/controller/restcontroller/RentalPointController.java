package com.senla.controller.restcontroller;

import com.senla.controller.mapper.RentalPointMapper;
import com.senla.domain.dto.RentalPointDto;
import com.senla.domain.dto.creation.RentalPointCreationDto;
import com.senla.domain.dto.update.GeolocationUpdateDto;
import com.senla.domain.model.entity.Geolocation;
import com.senla.domain.model.entity.RentalPoint;
import com.senla.service.RentalPointService;
import com.senla.utils.Geocoder;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/rental-points", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class RentalPointController {

    private final RentalPointMapper rentalPointMapper;
    private final RentalPointService rentalPointService;
    private final Geocoder geocoder;

    /**
     * Getting the list of rental points by selection options. <br>
     * <b>NOTE:</b> Geolocations does not contain null values - if the field's value is not specified,
     * it has default value. For the String, it's empty string "". <br><br>
     * <code>
     *      Example:<br>
     *      /v1/rental-points?orderBy=latitude&amp;asc=false&amp;limit=2 <br>
     *      { <br>
     *           "countryCode": "RUS", <br>
     *           "district": "МО Замоскворечье" <br>
     *       } <br>
     * </code>
     * @param orderBy Field's name of {@link Geolocation} class which is used for ordering result list
     * @param asc true or false
     * @param limit Limiting the size of result list.
     * @return selected <code>List&lt;RentalPointDto&gt;</code>
     * */
    @ApiOperation("Get all rental points by various filters")
    @GetMapping
    public List<RentalPointDto> getAll(@RequestParam(value = "asc", defaultValue = BooleanUtils.TRUE, required = false) boolean asc,
                                       @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
                                       @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                       @RequestParam(value = "countryCode", required = false) String countryCode,
                                       @RequestParam(value = "countryName", required = false) String countryName,
                                       @RequestParam(value = "county", required = false) String county,
                                       @RequestParam(value = "city", required = false) String city,
                                       @RequestParam(value = "district", required = false) String district,
                                       @RequestParam(value = "street", required = false) String street,
                                       @RequestParam(value = "houseNumber", required = false) String houseNumber,
                                       @RequestParam(value = "description", required = false) String description) {
        Map<String, Object> selectParameters = new HashMap<>() {{
                put("countryCode", countryCode);
                put("countryName", countryName);
                put("county", county);
                put("city", city);
                put("district", district);
                put("street", street);
                put("houseNumber", houseNumber);
                put("description", description);
        }};
        List<Geolocation> selectedGeolocations = rentalPointService.getAllGeo(selectParameters, orderBy, asc, limit);

        return selectedGeolocations
                .stream()
                .map(geolocation -> rentalPointMapper.convertToDto(geolocation.getRentalPoint()))
                .collect(toList());
    }

    /**
     * Getting the closest rental points by coordinates. <br><br>
     * <code>
     *    Example: <br>
     *    /v1/rental-points?lat=57&amp;lng=35&amp;limit=5
     * </code>
     * @param lat client's latitude
     * @param lng client's longitude
     * @param limit limiting result list size
     * @return <code>List&lt;{@link RentalPointDto}&gt;</code> of the closest rental points according to passed coordinates
     * */
    @GetMapping(value = "/closest", params = {"lat", "lng"})
    public List<RentalPointDto> getAllByCoordinates(@RequestParam(value = "lat") Double lat,
                                                    @RequestParam(value = "lng") Double lng,
                                                    @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit) {

        List<RentalPoint> closestRentalPoints = rentalPointService.getAllTheClosest(lat, lng, limit);

        return  closestRentalPoints.stream()
                .map(rentalPoint -> {
                    RentalPointDto rentalPointDto = rentalPointMapper.convertToDto(rentalPoint);
                    rentalPointDto.setDistanceToClientInKm(rentalPointService.getDistanceToClientInKm(rentalPoint, lat, lng));
                    return rentalPointDto;
                })
                .collect(toList());
    }

    /**
     * Getting concrete rental point by its id.
     * <b>NOTE:</b> Normally scooters are not fetched, but this method returns rental point with all the current scooters.
     * <br><br>
     * <code>
     *     Example: /v1/rental-points?lat=57.2&amp;lng=37.3&amp;limit=5
     * </code>
     * @param id Path variable
     * @return Rental point with current scooters
     */
    @GetMapping(value = "/{id}")
    public RentalPointDto getById(@PathVariable("id") Long id) {
        RentalPoint rentalPoint = rentalPointService.getByIdWithScooters(id);
        return rentalPointMapper.convertToDto(rentalPoint);
    }

    /**
     * Creating rental point via passed {@link RentalPointCreationDto} object.
     * <br>
     * <b>Note:</b> not passed geolocation parameters will automatically convert to empty string.
     * <br><br>
     * <code>
     * Example: <br>
     * { <br>
     *   &emsp;"geolocation": { <br>
     *   &emsp;"latitude": 59.93137, <br>
     *   &emsp;"longitude":  30.36069, <br>
     *   &emsp;"countryName": "Россия", <br>
     *   &emsp;"city": "Санкт-Петербург", <br>
     *   &emsp;"description": "Точка проката у входа на станцию Площадь Восстания" <br>
     *   &emsp;} <br>
     * } <br>
     * </code>
     * @param rentalPointCreationDto DTO for creation RentalPoint
     */
    @PostMapping
    public void createRentalPoint(@RequestBody RentalPointCreationDto rentalPointCreationDto) {
        RentalPoint rentalPoint = rentalPointMapper.convertToRentalPoint(rentalPointCreationDto);
        rentalPointService.create(rentalPoint);
    }

    /**
     * Takes coordinates, gets address via HERE api, and create a rental point with that address. <br>
     * <code>
     *     Example: <br>
     *     /v1/rental-points?lat=59.93&amp;lng=30.36&amp;desc=Точка проката рядом с магазином KFC
     * </code>
     * @param lat rental point's latitude
     * @param lng rental point's longitude
     * @param desc optional parameters which can be set at geolocation's description. If not - will be filled with
     *             the label of the HERE api response's address. Recommend to set it for more accurate defining the
     *             location (e.g. mentioning the closest landmarks)
     */
    @PostMapping(params = {"lat", "lng"})
    public void createRentalPoint(@RequestParam(value = "lat") Double lat,
                                  @RequestParam(value = "lng") Double lng,
                                  @RequestParam(value = "desc", defaultValue = "", required = false) String desc) {

        Geolocation geolocation = geocoder.getGeolocationFromCoordinates(lat, lng);
        if (!Objects.equals(desc, "")) {
            geolocation.setDescription(desc);
        }

        RentalPoint rentalPoint = new RentalPoint();
        rentalPoint.setGeolocation(geolocation);
        rentalPointService.create(rentalPoint);
    }

    /**
     * Updating rental point via passed {@link GeolocationUpdateDto} object
     * <b>NOTE:</b> skipped values are automatically set to null and null values <b>are never set to entity</b>.
     * @param id rental point's identifier
     * @param updateModel instance of {@link GeolocationUpdateDto} to take fields and their values for updating from
     */
    @PatchMapping(value = "/{id}")
    public void updateGeolocationOfRentalPoint(@PathVariable("id") Long id, @RequestBody GeolocationUpdateDto updateModel) {
        RentalPoint rentalPoint = rentalPointService.getById(id);
        rentalPointService.updateEntityFromDto(rentalPoint.getGeolocation(), updateModel, Geolocation.class);
        rentalPointService.update(rentalPoint);
    }

    /**
     * @param id Identifier of the deleting rental point
     */
    @DeleteMapping(value = "/{id}")
    public void deleteRentalPoint(@PathVariable("id") Long id) {
        rentalPointService.deleteById(id);
    }
}
