package com.travelbnbtest.travelbnbtest.controller;

import com.travelbnbtest.travelbnbtest.entity.AppUser;
import com.travelbnbtest.travelbnbtest.payload.BookingDto;
import com.travelbnbtest.travelbnbtest.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestParam long propertyId,
            @AuthenticationPrincipal AppUser user,
            @RequestBody BookingDto bookingDto
            ){
        BookingDto createdBooking = bookingService.createBooking(propertyId, user, bookingDto);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }
}
