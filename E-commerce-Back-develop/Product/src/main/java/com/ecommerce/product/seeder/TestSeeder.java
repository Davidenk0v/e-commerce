package com.ecommerce.product.seeder;

import static java.util.Map.entry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Image;
import com.ecommerce.product.entity.Like;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Rating;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.ImageRepository;
import com.ecommerce.product.repository.LikeRepository;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.RatingRepository;
import com.ecommerce.product.repository.SpecRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("test")
public class TestSeeder implements CommandLineRunner {
	
	// Repositories
	private final SpecRepository specRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final ModelRepository modelRepository;
	private final RatingRepository ratingRepository;
	private final CommentaryRepository commentaryRepository;
	private final LikeRepository likeRepository;
	private final ImageRepository imageRepository;
	
	private static final List<String> USER_IDS = List.of(
											"569g65de-35y6-4552-ac54-e52085109818", 
											"555f66de-36b6-4332-ac51-e52012661623",
											"432u67je-36r6-4532-ac52-e43246489998");
	
	private static final Map<String, String> IMAGE_URLS = Map.ofEntries(
											entry("portatil", "1657783580839-portatil.png"), 
											entry("sobremesa", "1657783580839-sobremesa.png"), 
											entry("smartphone", "1657783580839-smartphone.png"),
											entry("auriculares", "1657783580839-auriculares.png"),
											entry("camara", "1657783580839-camara.png"),
											entry("tablet", "1657783580839-tablet.png"));
	
	private static final String NEGRO = "Negro";
	private static final String BLANCO = "Blanco";
	private static final String COLOR = "Color";
	private static final String MACBOOKPRO = "Macbook Pro 2021";
	private static final String IPHONE13 = "iPhone 13";

	@Override
	@Transactional	
	public void run(String... args) throws Exception {
		// Seed specs
		specRepository.saveAll(List.of(
			    Spec.builder().name(COLOR).value(NEGRO).build(),
			    Spec.builder().name(COLOR).value(BLANCO).build()));
		
		// Seed categories
		categoryRepository.saveAll(List.of(
				Category.builder().title("Portátiles").build(),
				Category.builder().title("Smartphones").build()));
		
		// Seed products
		Category portatilesCat = new Category();
		Category smartphonesCat = new Category();
		Optional<Category> portatilesCatOpt = categoryRepository.findByTitle("Portátiles");
		if (portatilesCatOpt.isPresent()) {
			portatilesCat = portatilesCatOpt.get();
		}
		Optional<Category> smartphonesCatOpt = categoryRepository.findByTitle("Smartphones");
		if (smartphonesCatOpt.isPresent()) {
			smartphonesCat = smartphonesCatOpt.get();
		}
		
		productRepository.saveAll(List.of(
			    Product.builder()
			        .title(MACBOOKPRO)
			        .sellerId(USER_IDS.getFirst())
			        .categories(List.of(portatilesCat))
			        .manufacturer("Apple")
			        .build(),
			    Product.builder()
			        .title(IPHONE13)
			        .sellerId(USER_IDS.getFirst())
			        .categories(List.of(smartphonesCat))
			        .manufacturer("Apple")
			        .build()));
		
		// Seed Images
		imageRepository.saveAll(List.of(
				Image.builder().url(IMAGE_URLS.get("portatil")).build(),
				Image.builder().url(IMAGE_URLS.get("smartphone")).build()));
		
		// Seed the models
		Product macbookPro = new Product();
		Product iphone13 = new Product();
		Optional<Product> macbookProOpt = productRepository.findByTitle(MACBOOKPRO);
		if (macbookProOpt.isPresent()) {
			macbookPro = macbookProOpt.get();
		}
		Optional<Product> iphone13Opt = productRepository.findByTitle(IPHONE13);
		if (iphone13Opt.isPresent()) {
			iphone13 = iphone13Opt.get();
		}
		Image portatilImage = new Image();
		Image smartphoneImage = new Image();
		Optional<Image> portatilImageOpt = imageRepository.findById(Long.valueOf(1));
		if (portatilImageOpt.isPresent()) {
			portatilImage = portatilImageOpt.get();
		}
		Optional<Image> smartphoneImageOpt = imageRepository.findById(Long.valueOf(2));
		if (smartphoneImageOpt.isPresent()) {
			smartphoneImage = smartphoneImageOpt.get();
		}
		Spec negroSpec = new Spec();
		Spec blancoSpec = new Spec();
		Optional<Spec> negroSpecOpt = specRepository.findByNameAndValue(COLOR, NEGRO);
		if (negroSpecOpt.isPresent()) {
			negroSpec = negroSpecOpt.get();
		}
		Optional<Spec> blancoSpecOpt = specRepository.findByNameAndValue(COLOR, BLANCO);
		if (blancoSpecOpt.isPresent()) {
			blancoSpec = blancoSpecOpt.get();
		}
		modelRepository.saveAll(List.of(
			    Model.builder()
			        .product(macbookPro)
			        .price(BigDecimal.valueOf(2000.0))
			        .images(List.of(portatilImage))
			        .specs(List.of(negroSpec))
			        .description("Portátil de última generación en negro azabache, estética lujosa y alta capacidad con 32GB de RAM y memoria SSD de 1TB.")
			        .state(ModelState.PENDING_APPROVAL)
			        .build(),
		        Model.builder()
			        .product(iphone13)
			        .price(BigDecimal.valueOf(999.0))
			        .images(List.of(smartphoneImage))
			        .specs(List.of(blancoSpec))
			        .description("Smartphone de última generación con 256GB de almacenamiento y diseño elegante en blanco.")
			        .state(ModelState.PENDING_APPROVAL)
			        .build()));
		
		// Seed Ratings
		ratingRepository.saveAll(List.of(
			    Rating.builder()
			        .userId(USER_IDS.get(0))
			        .model(modelRepository.findByProductTitle(MACBOOKPRO).get(0))
			        .value(4)
			        .build(),
			    Rating.builder()
			        .userId(USER_IDS.get(1))
			        .model(modelRepository.findByProductTitle(MACBOOKPRO).get(0))
			        .value(5)
			        .build(),
			    Rating.builder()
			        .userId(USER_IDS.get(0))
			        .model(modelRepository.findByProductTitle(IPHONE13).get(0))
			        .value(5)
			        .build(),
			    Rating.builder()
			        .userId(USER_IDS.get(1))
			        .model(modelRepository.findByProductTitle(IPHONE13).get(0))
			        .value(4)
			        .build()));
		
		// Seed Commentaries
		commentaryRepository.saveAll(List.of(
                Commentary.builder()
                    .userId(USER_IDS.get(0))
                    .model(modelRepository.findByProductTitle(MACBOOKPRO).get(0))
                    .text("Excelente portátil, muy contento con la compra.")
                    .build(),
                Commentary.builder()
                    .userId(USER_IDS.get(1))
                    .model(modelRepository.findByProductTitle(IPHONE13).get(0))
                    .text("El iPhone es muy bonito, la pantalla es increíble.")
                    .build()));
		
		// Create a commentary with a parent commentary
		Optional<Commentary> optCommentary = commentaryRepository.findById((long) 2);
		if (optCommentary.isPresent()) {
			commentaryRepository.save(
				Commentary.builder()
			        .userId(USER_IDS.get(2))
			        .model(modelRepository.findByProductTitle(IPHONE13).get(0))
			        .text("El iPhone es muy bonito, la pantalla es increíble.")
			        .parentCommentary(optCommentary.get())
			        .build());
		}
        
		
		// Seed Likes
		likeRepository.saveAll(List.of(
				Like.builder()
					.userId(USER_IDS.get(0))
					.commentary(commentaryRepository.findByUserId(USER_IDS.get(1)).getFirst())
					.build(),
				Like.builder()
					.userId(USER_IDS.get(1))
					.commentary(commentaryRepository.findByUserId(USER_IDS.get(0)).getLast())
					.build()));
	}
}
