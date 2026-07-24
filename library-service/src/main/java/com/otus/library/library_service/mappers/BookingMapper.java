package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.response.BookingRespDto;
import com.otus.library.library_service.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    BookingRespDto toResponse(Booking booking);
}
