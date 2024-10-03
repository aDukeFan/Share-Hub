package ru.luckyskeet.sharehub.item;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.luckyskeet.sharehub.item.dto.CommentDtoIncome;
import ru.luckyskeet.sharehub.item.dto.CommentDtoOutcome;
import ru.luckyskeet.sharehub.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toSave(CommentDtoIncome commentDtoIncome);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "created", source = "createdTime")
    CommentDtoOutcome toSend(Comment comment);
}
