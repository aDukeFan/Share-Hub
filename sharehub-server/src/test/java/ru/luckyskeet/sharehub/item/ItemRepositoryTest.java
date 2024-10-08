package ru.luckyskeet.sharehub.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.luckyskeet.sharehub.item.model.Item;
import ru.luckyskeet.sharehub.request.RequestRepository;
import ru.luckyskeet.sharehub.request.model.Request;
import ru.luckyskeet.sharehub.user.UserRepository;
import ru.luckyskeet.sharehub.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DirtiesContext
    public void findByOwnerIdTest() {
        User user = new User()
                .setEmail("ya@ya.ru")
                .setName("name");
        User owner = userRepository.save(user);
        Item item = new Item()
                .setOwner(owner)
                .setName("Book War and Peace")
                .setAvailable(true)
                .setDescription("About life, Tolstoy");
        itemRepository.save(item);
        assertTrue(itemRepository.findByOwnerId(2L, PageRequest.of(0, 100)).isEmpty());
        assertEquals(1,
                itemRepository.findByOwnerId(1L, PageRequest.of(0, 100)).size());
    }

    @Test
    @DirtiesContext
    public void searchTest() {
        User user = new User()
                .setEmail("ya@ya.ru")
                .setName("name");
        User owner = userRepository.save(user);
        Item item = new Item()
                .setOwner(owner)
                .setName("Book War and Peace")
                .setAvailable(true)
                .setDescription("About life, Tolstoy");
        itemRepository.save(item);
        assertEquals(1, itemRepository
                .findAllByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(
                        "war",
                        "fire",
                        PageRequest.of(0, 100)).size());
    }

    @Test
    @DirtiesContext
    public void findByRequestIdTest() {
        User user = new User()
                .setEmail("ya@ya.ru")
                .setName("name");
        User owner = userRepository.save(user);
        User user2 = new User()
                .setEmail("ya2@ya.ru")
                .setName("name2");
        User requester = userRepository.save(user2);
        Request request = new Request()
                .setRequester(requester)
                .setCreated(LocalDateTime.now())
                .setDescription("I need book");
        long requestId = requestRepository.save(request).getId();
        Item item = new Item()
                .setOwner(owner)
                .setName("Book War and Peace")
                .setAvailable(true)
                .setDescription("About life, Tolstoy")
                .setRequestId(requestId);
        itemRepository.save(item);
        assertTrue(itemRepository.findByRequestId(2L).isEmpty());
        assertEquals(1, itemRepository.findByRequestId(1L).size());
    }
}