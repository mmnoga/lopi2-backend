package com.liftoff.project.command;

import com.liftoff.project.model.Cart;
import com.liftoff.project.model.CartItem;
import com.liftoff.project.model.Category;
import com.liftoff.project.model.ImageAsset;
import com.liftoff.project.model.Product;
import com.liftoff.project.model.ProductStatus;
import com.liftoff.project.model.Session;
import com.liftoff.project.model.User;
import com.liftoff.project.repository.CartItemRepository;
import com.liftoff.project.repository.CartRepository;
import com.liftoff.project.repository.CategoryRepository;
import com.liftoff.project.repository.ImageAssetRepository;
import com.liftoff.project.repository.ProductRepository;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.repository.UserRepository;
import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Component
@Priority(1)
@AllArgsConstructor
@Profile("!prod & !test")
public class ProductInsertCommand implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageAssetRepository imageAssetRepository;
    private final SessionRepository sessionRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;



    @Override
    public void run(String... args) {
        Category kawa = Category.builder()
                .uId(UUID.randomUUID())
                .name("Kawa")
                .description("Kawy ziarniste, mielone, rozpuszczalne.")
                .build();
        Category herbata = Category.builder()
                .uId(UUID.randomUUID())
                .name("Herbata")
                .description("English tea shop, Dilmah, Richmont, akcesoria do herbaty.")
                .build();
        Category yerba = Category.builder()
                .uId(UUID.randomUUID())
                .name("Yerba")
                .description("Yerba mate, akcesoria.")
                .build();
        Category ekspresy = Category.builder()
                .uId(UUID.randomUUID())
                .name("Ekspresy i akcesoria")
                .description("Akcesoria do kawy, herbaty i yerba mate. Ekspresy i młynki. Saturatory.")
                .build();
        Category kawaZiarnista = Category.builder()
                .uId(UUID.fromString("bfb969b7-05a2-45a6-887b-e83ef7b92574"))
                .name("Kawa ziarnista")
                .description("Kawa ziarnista to klasyka gatunku spośród wszystkich znanych obecnie rodzajów kawy.")
                .parentCategory(kawa)
                .build();
        Category kawaMielona = Category.builder()
                .uId(UUID.randomUUID())
                .name("Kawa mielona")
                .description("Kawa mielona jest efektem starannej i długotrwałej pracy specjalistów " +
                        "z najbardziej cenionych palarni kawy na całym świecie.")
                .parentCategory(kawa)
                .build();
        Category kawaRozpuszczalna = Category.builder()
                .uId(UUID.randomUUID())
                .name("Kawa rozpuszczalna")
                .description("Najlepsza kawa rozpuszczalna pozwala na szybkie oraz łatwe przygotowanie smacznego " +
                        "i gorącego napoju bez względu gdzie się znajdujemy.")
                .parentCategory(kawa)
                .build();
        Category englishTeaShop = Category.builder()
                .uId(UUID.randomUUID())
                .name("English Tea Shop")
                .description("English Tea Shop to marka założona w 2010 roku, " +
                        "której celem jest dostarczanie najwyższej jakości herbacianych mieszanek.")
                .parentCategory(herbata)
                .build();
        Category englishTeaShopLisciaste = Category.builder()
                .uId(UUID.randomUUID())
                .name("Liściaste")
                .parentCategory(englishTeaShop)
                .build();
        Category englishTeaShopTorebki = Category.builder()
                .uId(UUID.randomUUID())
                .name("Torebki")
                .parentCategory(englishTeaShop)
                .build();
        Category akcesoriaDoHerbaty = Category.builder()
                .uId(UUID.randomUUID())
                .name("Akcesoria do herbaty")
                .parentCategory(herbata)
                .build();
        Category herbataAkcesoriaFiltry = Category.builder()
                .uId(UUID.randomUUID())
                .name("Filtry")
                .parentCategory(akcesoriaDoHerbaty)
                .build();
        Category herbataAkcesoriaSzklanki = Category.builder()
                .uId(UUID.randomUUID())
                .name("Szklanki, kubki")
                .parentCategory(akcesoriaDoHerbaty)
                .build();
        categoryRepository.saveAll(List.of(kawa, herbata, yerba, ekspresy,
                kawaZiarnista, kawaMielona, kawaRozpuszczalna,
                englishTeaShop, englishTeaShopLisciaste, englishTeaShopTorebki,
                akcesoriaDoHerbaty, herbataAkcesoriaFiltry, herbataAkcesoriaSzklanki));

        ImageAsset melittaImage1 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2F7fa34810-0bd9-41d5-b6f1-f56fd218f388.jpg?generation=1692295048031287&alt=media")
                .build();
        ImageAsset melittaImage2 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2Fa1d764d8-037f-42fd-a178-fe70a4da80f9.png?generation=1692295115642575&alt=media")
                .build();

        imageAssetRepository.saveAll(List.of(melittaImage1, melittaImage2));

        Product kawaMelitta = Product.builder()
                .uId(UUID.fromString("b5fba84e-1729-4d99-8412-8421d44a2a85"))
                .name("Melitta BellaCrema Espresso 1 kg")
                .description("Kawa Melitta BellaCrema Espresso to wyjątkowa kompozycja najszlachetniejszych, " +
                        "starannie dobranych ziaren gatunku Arabica (100%). Dzięki mocnemu paleniu, " +
                        "charakterystycznemu dla kaw typu espresso, jest to mieszanka o " +
                        "najintensywniejszym smaku.")
                .sku("0128")
                .regularPrice(43.99)
                .discountPrice(40.00)
                .discountPriceEndDate(LocalDateTime.now().plusDays(10))
                .lowestPrice(40.00)
                .shortDescription("Kawa Melitta BellaCrema Espresso to wyjątkowa kompozycja " +
                        "najszlachetniejszych i starannie dobranych ziaren gatunku Arabica (100%).")
                .status(ProductStatus.ACTIVE)
                .quantity(100)
                .categories(List.of(kawaZiarnista))
                .images(List.of(melittaImage1, melittaImage2))
                .build();

        productRepository.save(kawaMelitta);

        ImageAsset mildanoImage1 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2F0d04d744-c83c-434a-ad8d-3591a33a24ef.jpeg?generation=1692295199044423&alt=media")
                .build();
        imageAssetRepository.save(mildanoImage1);

        Product kawaMildano = Product.builder()
                .uId(UUID.fromString("143a761d-2015-48fb-96bf-b02e04752bd4"))
                .name("MK Cafe Mildano 0,25 kg")
                .description("MK Cafe Mildano to doskonała kawa bezkofeinowa o wyjątkowo pełnym smaku " +
                        "i aromacie. Wyróżnia się nie tylko naturalnością, ale przede wszystkim wyczuwalnymi " +
                        "nutami kakao, toffi oraz cukru trzcinowego.")
                .sku("4991")
                .regularPrice(12.88)
                .discountPrice(11.00)
                .discountPriceEndDate(LocalDateTime.now().plusDays(10))
                .lowestPrice(10.00)
                .shortDescription("MK Cafe Mildano to doskonała kawa bezkofeinowa o wyjątkowo pełnym smaku i aromacie.")
                .status(ProductStatus.ACTIVE)
                .quantity(100)
                .categories(List.of(kawaMielona))
                .images(List.of(mildanoImage1))
                .build();

        productRepository.save(kawaMildano);

        ImageAsset davidoffImage1 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2F053d572f-4e91-4c3b-b3bb-c3a56acbe369.png?generation=1692295632904709&alt=media")
                .build();
        ImageAsset davidoffImage2 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2F13018714-7a1c-4708-ba39-004c5121678a.png?generation=1692295691288884&alt=media")
                .build();
        imageAssetRepository.saveAll(List.of(davidoffImage1, davidoffImage2));

        Product kawaDavidoff = Product.builder()
                .uId(UUID.randomUUID())
                .name("Davidoff Espresso Intense 57 100 g")
                .description("Davidoff Espresso Intense 57 to  kompozycja pochodząca z renomowanej " +
                        "i docenianej na całym świecie palarni. Dobrano do niej wysokiej jakości ziarna " +
                        "Arabica (100%).")
                .sku(" 4831")
                .regularPrice(23.90)
                .discountPrice(22.90)
                .discountPriceEndDate(LocalDateTime.now().plusDays(15))
                .lowestPrice(22.90)
                .shortDescription("Davidoff Espresso Intense 57 to  kompozycja pochodząca z renomowanej " +
                        "i docenianej na całym świecie palarni.")
                .status(ProductStatus.ACTIVE)
                .quantity(25)
                .categories(List.of(kawaRozpuszczalna))
                .images(List.of(davidoffImage1, davidoffImage2))
                .build();

        productRepository.save(kawaDavidoff);

        ImageAsset mkImage1 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2Fe7781aca-3bf2-4646-9e60-70917e4131e8.png?generation=1692296025764900&alt=media")
                .build();
        ImageAsset mkImage2 = ImageAsset.builder()
                .assetUrl("https://storage.googleapis.com/download/storage/v1/b/lopi-2-dev.appspot.com/o/images%2F3b9696f4-5d8c-4475-a147-5b2bbaae28af.png?generation=1692296083883569&alt=media")
                .build();
        imageAssetRepository.saveAll(List.of(mkImage1, mkImage2));

        Product kawaMK = Product.builder()
                .uId(UUID.randomUUID())
                .name("MK Cafe Premium Crema 130 g")
                .description("MK Cafe Premium Crema to znakomita kompozycja o wyraźnych nutach miodu, " +
                        "orzechów i karmelu. Doskonale kremowa w konsystencji, jednocześnie aromatyczna i lekka.")
                .sku(" 4884")
                .regularPrice(16.99)
                .discountPrice(15.99)
                .discountPriceEndDate(LocalDateTime.now().plusDays(5))
                .lowestPrice(15.99)
                .shortDescription("MK Cafe Premium Crema to znakomita kompozycja o wyraźnych " +
                        "nutach miodu, orzechów i karmelu.")
                .status(ProductStatus.ACTIVE)
                .quantity(25)
                .categories(List.of(kawaRozpuszczalna))
                .images(List.of(mkImage1, mkImage2))
                .build();

        productRepository.save(kawaMK);

        Product testProductIN_PREPARATION = Product.builder()
                .uId(UUID.fromString("da4611cc-9a91-44fc-9abe-47d2178cbfb3"))
                .name("TEST PRODUCT 1 (IN_PREPARATION status)")
                .description("Test product 1 description")
                .status(ProductStatus.IN_PREPARATION)
                .build();
        Product testProductCLOSED = Product.builder()
                .uId(UUID.fromString("f12d6c80-22a6-4ede-9088-6a72eb7e4ef0"))
                .name("TEST PRODUCT 2 (CLOSED status)")
                .description("Test product 2 description")
                .status(ProductStatus.CLOSED)
                .build();
        Product testProductACTIVE = Product.builder()
                .uId(UUID.fromString("198e4fbe-4413-4a9a-8aeb-f5002ab5ee62"))
                .name("TEST PRODUCT 3 (ACTIVE status)")
                .description("Test product 3 description")
                .status(ProductStatus.ACTIVE)
                .build();
        productRepository.saveAll(List.of(
                testProductIN_PREPARATION,
                testProductCLOSED,
                testProductACTIVE));



        ///////////
        Session session1 = new Session();
        session1.setId(1L);
        session1.setUId(UUID.fromString("7b2b9688-de45-445e-bdf3-a0a7d4ffe733"));
        session1.setExpirationTime(Instant.now().plus(30, ChronoUnit.DAYS));
        session1.setExpired(false);
        sessionRepository.save(session1);

        Cart cart1 = new Cart();
        cart1.setUuid(UUID.fromString("7b2b9688-de45-445e-bdf3-a0a7d4ffe733"));
        cart1.setUser(null);
        cart1.setTotalPrice(150.0);
        cart1.setTotalQuantity(3);
        cart1.setSession(session1);

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(kawaDavidoff);
        cartItem1.setQuantity(2);


        cart1.setCartItems(List.of(cartItem1));
        cartRepository.save(cart1);

        ////////////////////////////////

        User user = userRepository.findById(1L).orElse(null);

        Cart cart2 = new Cart();
        cart2.setUuid(UUID.fromString("7b2b9688-de45-445e-bdf3-a0a7d4ffe734"));
        cart2.setUser(user);
        cart2.setTotalPrice(300.00);
        cart2.setTotalQuantity(3);

        CartItem cartItem2 = new CartItem();
        cartItem2.setProduct(kawaMK);
        cartItem2.setQuantity(3);


        CartItem cartItem3 = new CartItem();
        cartItem3.setProduct(kawaMildano);
        cartItem3.setQuantity(1);

        cart2.setCartItems(List.of(cartItem2, cartItem3));
        cartRepository.save(cart2);




    }

}