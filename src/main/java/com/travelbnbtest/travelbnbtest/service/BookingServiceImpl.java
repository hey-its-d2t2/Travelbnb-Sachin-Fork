package com.travelbnbtest.travelbnbtest.service;

import com.travelbnbtest.travelbnbtest.entity.AppUser;
import com.travelbnbtest.travelbnbtest.entity.Booking;
import com.travelbnbtest.travelbnbtest.entity.Property;
import com.travelbnbtest.travelbnbtest.exception.ResourceNotFoundException;
import com.travelbnbtest.travelbnbtest.payload.BookingDto;
import com.travelbnbtest.travelbnbtest.repository.BookingRepository;
import com.travelbnbtest.travelbnbtest.repository.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService{
        private BookingRepository bookingRepository;
        private PropertyRepository propertyRepository;
        private PDFService pdfService;

    public BookingServiceImpl(BookingRepository bookingRepository, PropertyRepository propertyRepository, PDFService pdfService) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.pdfService = pdfService;
    }

    @Override
    public BookingDto createBooking(long propertyId, AppUser user, BookingDto bookingDto) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );
        bookingDto.setProperty(property);
        bookingDto.setAppUser(user);

        int nightlyPrice = property.getPrice();
        int totalPrice = nightlyPrice * bookingDto.getTotalNights();
        int gstAmount = (totalPrice * 18) / 100;
        int finalPrice = totalPrice + gstAmount;
        bookingDto.setPrice(finalPrice);

        Booking booking = dtoToEntity(bookingDto);
        Booking savedBooking = bookingRepository.save(booking);
        pdfService.generatePDF("D://SK//" +"booking-confirmation-id" +".pdf",bookingDto);
        BookingDto dto = entityToDto(savedBooking);
        return dto;

    }
    //dto to entity
    Booking dtoToEntity(BookingDto bookingDto){
        Booking booking=new Booking();
        booking.setName(bookingDto.getName());
        booking.setEmail(bookingDto.getEmail());
        booking.setMobile(bookingDto.getMobile());
        booking.setPrice(bookingDto.getPrice());
        booking.setTotalNights(bookingDto.getTotalNights());
        booking.setProperty(bookingDto.getProperty());
        booking.setAppUser(bookingDto.getAppUser());
        
        return booking;
    }
    //entity to Dto
    BookingDto entityToDto(Booking booking){
        BookingDto dto=new BookingDto();
        dto.setId(booking.getId());
        dto.setName(booking.getName());
        dto.setEmail(booking.getEmail());
        dto.setMobile(booking.getMobile());
        dto.setTotalNights(booking.getTotalNights());
        dto.setPrice(booking.getPrice());
        dto.setProperty(booking.getProperty());
        dto.setAppUser(booking.getAppUser());
        return dto;
    }
}
