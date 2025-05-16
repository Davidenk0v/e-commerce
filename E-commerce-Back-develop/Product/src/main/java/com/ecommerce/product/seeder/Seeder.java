package com.ecommerce.product.seeder;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Commentary;
import com.ecommerce.product.entity.Like;
import com.ecommerce.product.entity.Model;
import com.ecommerce.product.entity.ModelState;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Rating;
import com.ecommerce.product.entity.Spec;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.CommentaryRepository;
import com.ecommerce.product.repository.LikeRepository;
import com.ecommerce.product.repository.ModelRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.repository.RatingRepository;
import com.ecommerce.product.repository.SpecRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

	// Repositories
	private final SpecRepository specRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final ModelRepository modelRepository;
	private final RatingRepository ratingRepository;
	private final CommentaryRepository commentaryRepository;
	private final LikeRepository likeRepository;
	
	private static final List<String> USER_IDS = List.of(
											"5089894c-4aaf-49dc-bda6-785ab66ad1b4", 
											"11969b65-c55d-4740-bd86-99e4e736b8ee",
											"f099d3b5-df34-4f91-9cdd-96e127b83d44");
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// If no categories exist assume that the database is empty and seed it 
		if (categoryRepository.count() == 0) {
			// Seed specs
			specRepository.saveAll(List.of(
				    Spec.builder().name("Color").value("Negro").build(),
				    Spec.builder().name("Color").value("Blanco").build(),
				    Spec.builder().name("Color").value("Plata").build(),
				    Spec.builder().name("RAM").value("16GB").build(),
				    Spec.builder().name("RAM").value("32GB").build(),
				    Spec.builder().name("OS").value("Windows 11").build(),
				    Spec.builder().name("OS").value("Sin Sistema operativo").build(),
				    Spec.builder().name("Procesador").value("Intel Core I5").build(),
				    Spec.builder().name("Procesador").value("Intel Core I7").build(),
				    Spec.builder().name("Almacenamiento").value("1TB SSD").build(),
				    Spec.builder().name("Almacenamiento").value("256GB").build(),
				    Spec.builder().name("OS").value("Windows 10").build(),
				    Spec.builder().name("OS").value("Ubuntu 20.04").build(),
				    Spec.builder().name("Procesador").value("AMD Ryzen 5").build(),
				    Spec.builder().name("Procesador").value("AMD Ryzen 7").build(),
				    Spec.builder().name("Almacenamiento").value("512GB SSD").build(),
				    Spec.builder().name("Almacenamiento").value("2TB HDD").build(),
				    Spec.builder().name("RAM").value("8GB").build(),
				    Spec.builder().name("Pantalla").value("15.6 pulgadas").build(),
				    Spec.builder().name("Pantalla").value("13.3 pulgadas").build()
				));
			
			// Seed categories
			categoryRepository.saveAll(List.of(
					Category.builder().title("Portátiles").build(),
					Category.builder().title("Smartphones").build(),
				    Category.builder().title("Sobremesa").build(),
				    Category.builder().title("Ratones").build(),
				    Category.builder().title("Accesorios").build(),
				    Category.builder().title("Impresoras").build(),
				    Category.builder().title("Monitores").build(),
				    Category.builder().title("Cámaras").build(),
				    Category.builder().title("Auriculares").build(),
				    Category.builder().title("Altavoces").build(),
				    Category.builder().title("Tablets").build(),
				    Category.builder().title("Tarjetas gráficas").build()
				));
			
			// Seed products
			productRepository.saveAll(List.of(
				    Product.builder()
				        .title("Macbook Pro 2021")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Portátiles").get()))
				        .manufacturer("Apple")
				        .build(),
				    Product.builder()
				        .title("Dell XPS 13")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Portátiles").get()))
				        .manufacturer("Dell")
				        .build(),
				    Product.builder()
				        .title("iPhone 13")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Smartphones").get()))
				        .manufacturer("Apple")
				        .build(),
				    Product.builder()
				        .title("Samsung Galaxy S21")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Smartphones").get()))
				        .manufacturer("Samsung")
				        .build(),
				    Product.builder()
				        .title("iPad Pro")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Tablets").get()))
				        .manufacturer("Apple")
				        .build(),
				    Product.builder()
				        .title("Microsoft Surface Pro 7")
				        .sellerId(USER_IDS.getFirst())
				        .categories(List.of(categoryRepository.findByTitle("Tablets").get()))
				        .manufacturer("Microsoft")
				        .build(),
				    Product.builder()
				        .title("Sony WH-1000XM4")
				        .sellerId(USER_IDS.getLast())
				        .categories(List.of(categoryRepository.findByTitle("Auriculares").get()))
				        .manufacturer("Sony")
				        .build(),
				    Product.builder()
				        .title("Bose QuietComfort 35 II")
				        .sellerId(USER_IDS.getLast())
				        .categories(List.of(categoryRepository.findByTitle("Auriculares").get()))
				        .manufacturer("Bose")
				        .build(),
				    Product.builder()
				        .title("Canon EOS R5")
				        .sellerId(USER_IDS.getLast())
				        .categories(List.of(categoryRepository.findByTitle("Cámaras").get()))
				        .manufacturer("Canon")
				        .build(),
				    Product.builder()
				        .title("HP Omen 15")
				        .sellerId(USER_IDS.getLast())
				        .categories(List.of(categoryRepository.findByTitle("Sobremesa").get()))
				        .manufacturer("HP")
				        .build()
				));
			
			// Seed the models
			modelRepository.saveAll(List.of(
				    Model.builder()
				        .product(productRepository.findByTitle("Macbook Pro 2021").get())
				        .price(BigDecimal.valueOf(2000.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(1)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("RAM", "32GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "1TB SSD").get()
				        ))
				        .description("<h2>MacBook Pro (2021). Potencia profesional con estilo atemporal.</h2><p><strong>Diseñado para los que exigen más.</strong><br>Descubre el MacBook Pro 2021 en su elegante acabado negro azabache, una joya tecnológica que combina diseño sofisticado con un rendimiento arrollador. Creado para los profesionales más exigentes, este portátil no solo brilla por fuera, sino que es una bestia por dentro.</p><p><strong>Rendimiento que impresiona.</strong><br>Equipado con <strong>32 GB de memoria unificada</strong> y un veloz <strong>SSD de 1 TB</strong>, este MacBook Pro es pura eficiencia. Ya sea que edites vídeo en 4K, trabajes con modelos 3D complejos o simplemente quieras una experiencia multitarea fluida, este equipo está listo para enfrentarlo todo.</p><p><strong>Más que potencia, inteligencia.</strong><br>Gracias al chip <strong>M1 Pro o M1 Max</strong> (según configuración), disfrutarás de un rendimiento hasta <strong>x2,5 veces superior</strong> al de generaciones anteriores. Su arquitectura avanzada optimiza cada proceso, permitiéndote trabajar, crear y disfrutar sin interrupciones.</p><p><strong>Pantalla que deslumbra.</strong><br>La pantalla <strong>Liquid Retina XDR</strong> de 14 o 16 pulgadas (según modelo) ofrece un brillo espectacular, negros profundos y una fidelidad cromática que conquista a diseñadores, fotógrafos y creadores de contenido.</p><p><strong>Diseñado para durar.</strong><br>Con una autonomía que alcanza hasta <strong>21 horas</strong>, el MacBook Pro 2021 te acompaña sin esfuerzo desde la mañana hasta bien entrada la noche. Y con carga rápida, siempre estarás listo para lo que venga.</p><p><strong>Conectividad total.</strong><br>Incluye puertos <strong>HDMI</strong>, lector de tarjetas <strong>SDXC</strong>, entrada para auriculares y múltiples puertos <strong>Thunderbolt 4</strong> para conectar monitores externos, discos duros y más. Todo con una velocidad y versatilidad sin precedentes.</p><p><strong>Más seguro. Más tuyo.</strong><br>Desbloquea el portátil con <strong>Touch ID</strong>, protege tus archivos con <strong>FileVault</strong>, y mantén todo sincronizado gracias al ecosistema Apple. Con <strong>macOS Monterey</strong> (actualizable a versiones más recientes), accede a un universo de apps profesionales y herramientas optimizadas para sacar el máximo partido a tu Mac.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Dell XPS 13").get())
				        .price(BigDecimal.valueOf(1500.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get(),
				            specRepository.findByNameAndValue("RAM", "16GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "512GB SSD").get()
				        ))
				        .description("<h2>Dell XPS 13. Rendimiento brillante en un diseño icónico.</h2><p><strong>Diseñado para destacar. Creado para durar.</strong><br>El <strong>Dell XPS 13</strong> es mucho más que un portátil compacto: es una declaración de estilo y rendimiento. Su chasis de aluminio mecanizado y sus bordes finamente pulidos hacen que cada detalle respire elegancia. Ultraligero, delgado y resistente, es el aliado perfecto para llevar la productividad a donde tú vayas.</p><p><strong>Potencia que te acompaña.</strong><br>Con <strong>16 GB de RAM</strong> y un veloz <strong>almacenamiento SSD de 512 GB</strong>, este portátil está preparado para ejecutar múltiples tareas, abrir aplicaciones al instante y ofrecer una experiencia fluida y sin interrupciones, incluso en los flujos de trabajo más exigentes. Ya sea para trabajo, estudio o creatividad, el XPS 13 responde con creces.</p><p><strong>Pantalla que enamora.</strong><br>Su deslumbrante pantalla <strong>InfinityEdge</strong> prácticamente sin bordes te sumerge en una experiencia visual nítida, brillante y realista. Ya sea en su versión <strong>Full HD+</strong> o <strong>4K Ultra HD+</strong>, cada píxel cobra vida con una precisión de color ideal para creadores, diseñadores y amantes del detalle.</p><p><strong>Movilidad sin límites.</strong><br>Gracias a su diseño ultraportátil y a una batería que puede alcanzar hasta <strong>14 horas de autonomía</strong>, el XPS 13 es ideal para jornadas completas de trabajo sin depender del cargador. Perfecto para quienes viven en movimiento.</p><p><strong>Conectividad moderna y segura.</strong><br>Equipado con puertos <strong>Thunderbolt 4</strong>, USB-C y lector de tarjetas microSD, garantiza compatibilidad con tus dispositivos y accesorios de alto rendimiento. Además, cuenta con sensor de huella para un inicio de sesión rápido y seguro.</p><p><strong>Tecnología que entiende tus necesidades.</strong><br>Optimizado con <strong>Windows 11</strong> y herramientas de inteligencia adaptativa de Dell, el XPS 13 aprende de tu uso y mejora el rendimiento, la refrigeración y la duración de la batería en función de tus hábitos.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("iPhone 13").get())
				        .price(BigDecimal.valueOf(999.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(3)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Smartphone de última generación. Elegancia y potencia en tu mano.</h2><p><strong>Diseño que enamora.</strong><br>Este smartphone destaca por su acabado <strong>blanco sofisticado</strong>, líneas refinadas y una ergonomía que se adapta perfectamente a tu mano. Un equilibrio perfecto entre estilo moderno y comodidad para el día a día.</p><p><strong>Espacio para todo.</strong><br>Con <strong>256 GB de almacenamiento</strong>, tendrás capacidad de sobra para guardar tus fotos, vídeos en alta resolución, aplicaciones y documentos sin preocuparte por el espacio. Lleva todo lo que necesitas siempre contigo.</p><p><strong>Rendimiento a la altura.</strong><br>Equipado con un procesador de última generación, este dispositivo ofrece una experiencia fluida, rápida y eficiente. Ideal para multitarea, juegos exigentes y navegación sin interrupciones.</p><p><strong>Pantalla que cautiva.</strong><br>Su pantalla de alta resolución brinda colores vibrantes, negros profundos y una nitidez excepcional, perfecta para disfrutar de contenido multimedia, videollamadas o redes sociales con la mejor calidad visual.</p><p><strong>Conectividad avanzada y seguridad.</strong><br>Con compatibilidad 5G, Wi-Fi de última generación y sensor de huella o reconocimiento facial, este smartphone te mantiene siempre conectado y protegido.</p><p><strong>Autonomía para todo el día.</strong><br>Gracias a su batería de larga duración y carga rápida, te acompaña desde la mañana hasta la noche sin interrupciones.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Samsung Galaxy S21").get())
				        .price(BigDecimal.valueOf(899.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Smartphone avanzado. Diseño moderno y alto rendimiento.</h2><p><strong>Estética impecable.</strong><br>Con un acabado <strong>blanco moderno</strong> y un diseño minimalista, este smartphone combina elegancia y funcionalidad. Su perfil delgado y materiales premium lo convierten en un dispositivo tan atractivo como cómodo de usar.</p><p><strong>Almacenamiento sin límites.</strong><br>Sus <strong>256 GB de capacidad</strong> te permiten guardar miles de fotos, vídeos, apps y documentos, asegurando espacio de sobra para tu contenido más importante.</p><p><strong>Potencia en cada tarea.</strong><br>Gracias a su procesador avanzado, disfrutarás de un rendimiento rápido y eficiente, ideal para juegos, edición de fotos, videollamadas o multitarea sin demoras.</p><p><strong>Experiencia visual superior.</strong><br>La pantalla de alta definición ofrece colores intensos, brillo envolvente y una nitidez perfecta para consumir contenido o trabajar desde cualquier lugar.</p><p><strong>Conectividad y seguridad de última generación.</strong><br>Incluye 5G, Wi-Fi rápido y desbloqueo seguro mediante huella o reconocimiento facial, para que siempre estés conectado y protegido.</p><p><strong>Batería que responde a tu ritmo.</strong><br>Su autonomía prolongada y sistema de carga rápida te garantizan energía todo el día, estés donde estés.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("iPad Pro").get())
				        .price(BigDecimal.valueOf(1200.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(6)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "1TB SSD").get()
				        ))
				        .description("<h2>Tablet de alto rendimiento. Sofisticación y potencia sin límites.</h2><p><strong>Diseño que inspira.</strong><br>Con un elegante acabado <strong>negro sofisticado</strong> y un cuerpo delgado de materiales premium, esta tablet combina estilo moderno y comodidad para llevar a todas partes.</p><p><strong>Almacenamiento masivo.</strong><br>Gracias a su <strong>1 TB de almacenamiento SSD</strong>, tendrás espacio de sobra para guardar tus documentos, vídeos, fotos, apps y proyectos creativos sin preocuparte por el espacio.</p><p><strong>Potencia profesional.</strong><br>Equipada con un procesador de última generación y memoria optimizada, esta tablet ofrece un rendimiento fluido para multitarea, edición de contenido, juegos avanzados y más.</p><p><strong>Pantalla envolvente.</strong><br>Su pantalla de alta definición ofrece colores vivos, gran brillo y detalles nítidos, ideal para disfrutar de contenido multimedia, leer o trabajar con precisión.</p><p><strong>Conectividad total.</strong><br>Incluye Wi-Fi rápido, puertos USB-C y compatibilidad con accesorios inteligentes, para una experiencia versátil tanto en casa como en movilidad.</p><p><strong>Autonomía para todo el día.</strong><br>Su batería de larga duración y carga rápida te permite trabajar, crear y entretenerte durante horas sin interrupciones.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Microsoft Surface Pro 7").get())
				        .price(BigDecimal.valueOf(1100.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Plata").get(),
				            specRepository.findByNameAndValue("RAM", "16GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "512GB SSD").get()
				        ))
				        .description("<h2>Tablet versátil. Rendimiento y elegancia en cada detalle.</h2><p><strong>Diseño refinado en plata.</strong><br>Con un acabado <strong>plateado</strong> y un chasis ligero y delgado, esta tablet combina sofisticación y portabilidad para adaptarse a cualquier entorno.</p><p><strong>Memoria para todo.</strong><br>Equipada con <strong>16 GB de RAM</strong> y un veloz <strong>SSD de 512 GB</strong>, ofrece capacidad y rapidez para gestionar múltiples tareas, almacenar contenido y disfrutar de una experiencia fluida en cualquier actividad.</p><p><strong>Potencia equilibrada.</strong><br>Su procesador avanzado garantiza un rendimiento óptimo tanto para productividad, creatividad como entretenimiento, permitiéndote trabajar o disfrutar sin interrupciones.</p><p><strong>Experiencia visual inmersiva.</strong><br>Su pantalla de alta resolución ofrece colores precisos y gran nitidez, ideal para visualizar documentos, vídeos o contenidos creativos con máxima claridad.</p><p><strong>Conectividad moderna.</strong><br>Con puertos USB-C, Wi-Fi rápido y compatibilidad con accesorios inteligentes, esta tablet está preparada para todas tus necesidades de conexión.</p><p><strong>Autonomía prolongada.</strong><br>Su batería de larga duración te acompaña durante jornadas completas, con carga rápida para que siempre estés listo.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Sony WH-1000XM4").get())
				        .price(BigDecimal.valueOf(350.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(4)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get()
				        ))
				        .description("<h2>Auriculares inalámbricos. Sonido envolvente con estilo.</h2><p><strong>Diseño sofisticado en negro.</strong><br>Con un acabado <strong>negro elegante</strong> y una estructura ergonómica, estos auriculares combinan comodidad y estética premium para acompañarte en cualquier ocasión.</p><p><strong>Cancelación de ruido avanzada.</strong><br>Gracias a su <strong>tecnología de cancelación activa de ruido</strong>, disfrutarás de un sonido nítido y sin distracciones, ideal para música, llamadas o concentración en entornos ruidosos.</p><p><strong>Audio de alta fidelidad.</strong><br>Drivers de última generación ofrecen bajos profundos, agudos claros y un equilibrio sonoro que transforma cada escucha en una experiencia envolvente.</p><p><strong>Conectividad sin límites.</strong><br>Con <strong>Bluetooth de última generación</strong>, se emparejan fácilmente con tus dispositivos para una conexión estable y de baja latencia.</p><p><strong>Autonomía que acompaña tu ritmo.</strong><br>Hasta <strong>30 horas de reproducción</strong> con el estuche de carga y carga rápida para que nunca te quedes sin música.</p><p><strong>Controles intuitivos.</strong><br>Gestos táctiles y asistentes de voz integrados facilitan el control de la música, las llamadas y más, con un solo toque.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Bose QuietComfort 35 II").get())
				        .price(BigDecimal.valueOf(300.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Plata").get()
				        ))
				        .description("<h2>Auriculares con cancelación de ruido. Comodidad y claridad en cada nota.</h2><p><strong>Elegancia en plata.</strong><br>Con un <strong>diseño plateado</strong> y acolchados ergonómicos, estos auriculares ofrecen una estética moderna y una comodidad superior para uso prolongado.</p><p><strong>Cancelación activa de ruido.</strong><br>Su <strong>tecnología avanzada</strong> elimina ruidos externos, permitiéndote sumergirte en la música, llamadas o contenidos sin interrupciones.</p><p><strong>Sonido premium.</strong><br>Drivers optimizados garantizan un audio equilibrado, con <strong>bajos potentes</strong> y <strong>agudos nítidos</strong> para una experiencia auditiva envolvente.</p><p><strong>Conectividad eficiente.</strong><br>Compatible con <strong>Bluetooth</strong> y entrada por cable, se adapta a tus dispositivos con facilidad y ofrece una conexión estable.</p><p><strong>Autonomía prolongada.</strong><br>Disfruta de hasta <strong>25 horas de reproducción</strong> y función de carga rápida para seguir escuchando sin interrupciones.</p><p><strong>Controles prácticos.</strong><br>Botones intuitivos te permiten gestionar música y llamadas cómodamente, además de integrar soporte para asistentes de voz.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Canon EOS R5").get())
				        .price(BigDecimal.valueOf(3500.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(5)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Cámara profesional. Precisión y robustez para cada toma.</h2><p><strong>Diseño robusto en negro.</strong><br>Con un <strong>chasis resistente</strong> y acabado negro elegante, esta cámara está construida para soportar condiciones exigentes sin sacrificar estilo.</p><p><strong>Almacenamiento amplio.</strong><br>Sus <strong>256 GB de almacenamiento interno</strong> permiten capturar miles de fotos en alta resolución y largas sesiones de vídeo sin preocupaciones.</p><p><strong>Calidad de imagen superior.</strong><br>Equipada con <strong>sensor avanzado</strong> y óptica de precisión, ofrece detalles nítidos, colores vibrantes y un rango dinámico ideal para profesionales.</p><p><strong>Versatilidad creativa.</strong><br>Incluye <strong>modos manuales</strong>, grabación en <strong>4K</strong>, y compatibilidad con objetivos intercambiables para adaptarse a cada proyecto.</p><p><strong>Conectividad moderna.</strong><br>Con <strong>Wi-Fi, Bluetooth</strong> y puertos USB-C, facilita la transferencia de archivos y el control remoto desde dispositivos móviles.</p><p><strong>Autonomía confiable.</strong><br>Su batería de larga duración y opciones de carga rápida te permiten fotografiar durante toda la jornada sin interrupciones.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
			        Model.builder()
				        .product(productRepository.findByTitle("Macbook Pro 2021").get())
				        .price(BigDecimal.valueOf(2000.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("RAM", "32GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "1TB SSD").get()
				        ))
				        .description("<h2>Portátil de última generación. Potencia y elegancia en negro azabache.</h2><p><strong>Estética lujosa.</strong><br>Este <strong>portátil en negro azabache</strong> destaca por su diseño refinado, combinando sofisticación con la máxima calidad en cada detalle.</p><p><strong>Alto rendimiento.</strong><br>Con <strong>32 GB de RAM</strong> y un <strong>SSD de 1TB</strong>, ofrece una capacidad impresionante para ejecutar múltiples tareas, trabajar con aplicaciones exigentes y almacenar grandes volúmenes de datos sin esfuerzo.</p><p><strong>Velocidad y fluidez.</strong><br>Disfruta de una experiencia de usuario ágil y fluida, ideal para profesionales y creativos que necesitan la máxima potencia para editar, programar o diseñar.</p><p><strong>Diseño robusto y portátil.</strong><br>Compacto y ligero, este portátil es perfecto para llevarlo contigo y afrontar cualquier reto en cualquier lugar con facilidad y estilo.</p><p><strong>Conectividad avanzada.</strong><br>Incluye puertos de alta velocidad, como <strong>USB-C</strong> y <strong>Thunderbolt</strong>, para garantizar una conexión rápida y eficiente con dispositivos y accesorios.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Dell XPS 13").get())
				        .price(BigDecimal.valueOf(1500.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get(),
				            specRepository.findByNameAndValue("RAM", "16GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "512GB SSD").get()
				        ))
				        .description("<h2>Portátil compacto y potente. Rendimiento de alto nivel en un diseño compacto.</h2><p><strong>Diseño compacto y ligero.</strong><br>Este <strong>portátil compacto</strong> está diseñado para quienes necesitan potencia sin sacrificar portabilidad. Perfecto para llevarlo a cualquier parte sin esfuerzo.</p><p><strong>Rendimiento eficiente.</strong><br>Equipado con <strong>16 GB de RAM</strong> y un <strong>almacenamiento SSD de 512GB</strong>, este dispositivo ofrece una experiencia fluida y rápida, ideal para multitarea y tareas exigentes.</p><p><strong>Velocidad y capacidad.</strong><br>Disfruta de arranques rápidos, acceso instantáneo a archivos y una fluidez impresionante en todas tus aplicaciones diarias.</p><p><strong>Ideal para profesionales y estudiantes.</strong><br>Ya sea para trabajar, estudiar o disfrutar de contenido multimedia, este portátil es perfecto para maximizar tu productividad en todo momento.</p><p><strong>Conectividad versátil.</strong><br>Con puertos avanzados y opciones de conexión rápida, mantiene tu flujo de trabajo eficiente y sin interrupciones.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("iPhone 13").get())
				        .price(BigDecimal.valueOf(999.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Smartphone de última generación. Elegancia y rendimiento en negro.</h2><p><strong>Diseño elegante.</strong><br>Este <strong>smartphone en negro</strong> combina un diseño sofisticado con una construcción de alta calidad, ofreciendo una apariencia premium en cada detalle.</p><p><strong>Almacenamiento de gran capacidad.</strong><br>Con <strong>256 GB de almacenamiento</strong>, tendrás suficiente espacio para guardar tus fotos, videos y aplicaciones sin preocuparte por quedarte sin espacio.</p><p><strong>Rendimiento excepcional.</strong><br>Gracias a su potente hardware, este dispositivo te ofrece un rendimiento rápido y fluido para todas tus necesidades diarias, desde la multitarea hasta el uso de aplicaciones exigentes.</p><p><strong>Perfecto para el día a día.</strong><br>Diseñado para quienes buscan un equilibrio entre estilo y funcionalidad, ideal para quienes valoran tanto la estética como la tecnología de vanguardia.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Samsung Galaxy S21").get())
				        .price(BigDecimal.valueOf(899.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Plata").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Smartphone avanzado. Potencia y estilo en plata.</h2><p><strong>Diseño moderno.</strong><br>Este <strong>smartphone en plata</strong> destaca por su diseño elegante y contemporáneo, perfecto para quienes buscan tecnología avanzada con una apariencia sofisticada.</p><p><strong>Amplio almacenamiento.</strong><br>Con <strong>256 GB de almacenamiento</strong>, tendrás espacio más que suficiente para almacenar tus archivos, fotos, aplicaciones y todo lo que necesites, sin preocupaciones.</p><p><strong>Rendimiento sobresaliente.</strong><br>Equipado con tecnología de última generación, este smartphone te ofrece un rendimiento ágil y fluido, ideal para tareas multitarea, navegación y aplicaciones exigentes.</p><p><strong>Ideal para cada momento.</strong><br>Con un equilibrio perfecto entre diseño y rendimiento, es el dispositivo ideal para quienes buscan un teléfono inteligente que lo tenga todo: estilo, potencia y capacidad.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("iPad Pro").get())
				        .price(BigDecimal.valueOf(1200.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "1TB SSD").get()
				        ))
				        .description("<h2>Tablet de alto rendimiento. Potencia y sofisticación en blanco.</h2><p><strong>Diseño sofisticado.</strong><br>Con un acabado en <strong>blanco</strong> elegante, esta tablet no solo es un dispositivo potente, sino también un accesorio de diseño moderno y estilizado que se adapta a cualquier entorno.</p><p><strong>Amplio almacenamiento.</strong><br>Con <strong>1TB de almacenamiento SSD</strong>, tendrás espacio de sobra para guardar tus archivos, aplicaciones y multimedia, asegurando un acceso rápido y eficiente a todos tus contenidos.</p><p><strong>Rendimiento de alto nivel.</strong><br>Equipado con tecnología de última generación, esta tablet ofrece un rendimiento fluido y ágil para todo tipo de tareas, desde la navegación hasta el uso de aplicaciones exigentes.</p><p><strong>Ideal para profesionales y creativos.</strong><br>Perfecta para quienes necesitan un dispositivo potente y versátil, ideal tanto para trabajo como para entretenimiento, con un diseño que refleja sofisticación y elegancia.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Microsoft Surface Pro 7").get())
				        .price(BigDecimal.valueOf(1100.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("RAM", "16GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "512GB SSD").get()
				        ))
				        .description("<h2>Tablet versátil. Potencia y diseño en negro.</h2><p><strong>Diseño elegante.</strong><br>Con un acabado en <strong>negro</strong>, esta tablet combina un diseño moderno y profesional que se adapta perfectamente a cualquier estilo de vida.</p><p><strong>Gran capacidad.</strong><br>Con <strong>16GB de RAM</strong> y <strong>512GB de almacenamiento SSD</strong>, ofrece un rendimiento excepcional y espacio amplio para almacenar todos tus archivos, aplicaciones y contenido multimedia.</p><p><strong>Rendimiento sin límites.</strong><br>Ideal para multitarea y uso intensivo, esta tablet te permite trabajar, crear y disfrutar de tus contenidos con total fluidez y velocidad.</p><p><strong>Perfecta para cualquier ocasión.</strong><br>Versátil y poderosa, es el dispositivo ideal para profesionales, estudiantes o cualquier persona que busque un equipo de alto rendimiento en un formato compacto y elegante.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Sony WH-1000XM4").get())
				        .price(BigDecimal.valueOf(350.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Blanco").get()
				        ))
				        .description("<h2>Auriculares inalámbricos. Sonido nítido y diseño elegante en blanco.</h2><p><strong>Cancelación activa de ruido.</strong><br>Disfruta de una experiencia sonora inmersiva con la tecnología de <strong>cancelación de ruido</strong> que bloquea las distracciones externas, permitiéndote concentrarte en lo que más importa.</p><p><strong>Diseño elegante.</strong><br>Con un acabado <strong>blanco</strong> sofisticado, estos auriculares no solo ofrecen un sonido excepcional, sino que también destacan por su estilo moderno y refinado.</p><p><strong>Comodidad y rendimiento.</strong><br>Diseñados para adaptarse cómodamente a tus oídos durante largas sesiones de escucha, estos auriculares ofrecen un ajuste seguro y ligero, ideal para uso prolongado.</p><p><strong>Conectividad sin interrupciones.</strong><br>Con tecnología inalámbrica avanzada, podrás disfrutar de una conexión estable y sin interrupciones, perfecta para música, llamadas o videoconferencias.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Bose QuietComfort 35 II").get())
				        .price(BigDecimal.valueOf(300.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Plata").get()
				        ))
				        .description("<h2>Auriculares con cancelación de ruido. Comodidad y estilo en plata.</h2><p><strong>Cancelación activa de ruido.</strong><br>Sumérgete en un sonido puro con la tecnología de <strong>cancelación de ruido</strong>, que elimina las distracciones y mejora tu experiencia auditiva.</p><p><strong>Diseño cómodo.</strong><br>Con un diseño ergonómico y <strong>plata</strong> elegante, estos auriculares ofrecen un ajuste perfecto para largos períodos de uso sin incomodidad.</p><p><strong>Sonido excepcional.</strong><br>Disfruta de un audio claro y equilibrado, ideal para música, llamadas o películas, con un rendimiento superior en cada detalle.</p><p><strong>Conectividad sin límites.</strong><br>Equipados con tecnología inalámbrica, estos auriculares te ofrecen una conexión estable y libre de cables para que disfrutes de tu contenido sin interrupciones.</p>")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("Canon EOS R5").get())
				        .price(BigDecimal.valueOf(3500.0))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "256GB").get()
				        ))
				        .description("<h2>Cámara profesional. Rendimiento avanzado y diseño robusto en negro.</h2><p><strong>Alta capacidad de almacenamiento.</strong><br>Con <strong>256 GB</strong> de almacenamiento, tendrás espacio de sobra para guardar todas tus fotos y vídeos de alta resolución sin preocuparte por el espacio.</p><p><strong>Diseño robusto.</strong><br>Construida para soportar las exigencias del trabajo profesional, su diseño <strong>negro</strong> y duradero asegura resistencia y fiabilidad en cualquier entorno.</p><p><strong>Rendimiento excepcional.</strong><br>Captura imágenes nítidas y vídeos de alta calidad con una cámara profesional optimizada para cualquier situación, desde retratos hasta paisajes.</p><p><strong>Conectividad avanzada.</strong><br>Gracias a sus puertos y opciones de conectividad, puedes transferir tus imágenes y vídeos rápidamente a otros dispositivos para una edición y almacenamiento eficientes.</p>\r\n")
				        .state(ModelState.ON_SALE)
				        .build(),
				    Model.builder()
				        .product(productRepository.findByTitle("HP Omen 15").get())
				        .price(BigDecimal.valueOf(1800.0))
				        //.images(List.of(imageRepository.findById(Long.valueOf(2)).get()))
				        .specs(List.of(
				            specRepository.findByNameAndValue("Color", "Negro").get(),
				            specRepository.findByNameAndValue("RAM", "32GB").get(),
				            specRepository.findByNameAndValue("Almacenamiento", "1TB SSD").get()
				        ))
				        .description("<h2>Portátil gaming. Alto rendimiento y diseño agresivo en negro.</h2><p><strong>Potencia para los gamers.</strong><br>Con <strong>32 GB de RAM</strong> y un <strong>SSD de 1 TB</strong>, este portátil gaming ofrece un rendimiento inigualable para ejecutar los juegos más exigentes y aplicaciones pesadas con total fluidez.</p><p><strong>Diseño agresivo y elegante.</strong><br>Su diseño en <strong>negro</strong> combina estética futurista con robustez, ideal para los jugadores que buscan un equipo que se vea tan bien como rinde.</p><p><strong>Rendimiento sin límites.</strong><br>Equipada con componentes de última generación, esta máquina está preparada para ofrecerte una experiencia de juego inmersiva y sin interrupciones, ya sea en títulos AAA o juegos online competitivos.</p><p><strong>Conectividad avanzada.</strong><br>Gracias a sus puertos de alto rendimiento, puedes conectar monitores, periféricos y otros dispositivos con la máxima velocidad para mantener el control total en todo momento.</p>")
				        .state(ModelState.ON_SALE)
				        .build()
				));
			
			// Seed Ratings
			ratingRepository.saveAll(List.of(
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Samsung Galaxy S21").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Samsung Galaxy S21").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Samsung Galaxy S21").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("iPad Pro").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("iPad Pro").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("iPad Pro").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Microsoft Surface Pro 7").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Microsoft Surface Pro 7").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Microsoft Surface Pro 7").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Sony WH-1000XM4").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Sony WH-1000XM4").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Sony WH-1000XM4").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Bose QuietComfort 35 II").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Bose QuietComfort 35 II").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Bose QuietComfort 35 II").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("Canon EOS R5").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("Canon EOS R5").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("Canon EOS R5").get(0))
				        .value(3)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(0))
				        .model(modelRepository.findByProductTitle("HP Omen 15").get(0))
				        .value(5)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(1))
				        .model(modelRepository.findByProductTitle("HP Omen 15").get(0))
				        .value(4)
				        .build(),
				    Rating.builder()
				        .userId(USER_IDS.get(2))
				        .model(modelRepository.findByProductTitle("HP Omen 15").get(0))
				        .value(3)
				        .build()
				));
			
			// Seed Commentaries
			commentaryRepository.saveAll(List.of(
                    Commentary.builder()
                        .userId(USER_IDS.get(0))
                        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
                        .text("Excelente portátil, muy contento con la compra.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(1))
                        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
                        .text("Muy buena calidad, el envío fue rápido.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(2))
                        .model(modelRepository.findByProductTitle("Macbook Pro 2021").get(0))
                        .text("No me ha gustado mucho, esperaba más.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(0))
                        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
                        .text("Muy contento con la compra, el portátil es muy rápido.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(1))
                        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
                        .text("El portátil es muy bonito, la pantalla es increíble.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(2))
                        .model(modelRepository.findByProductTitle("Dell XPS 13").get(0))
                        .text("No me ha gustado mucho, esperaba más.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(0))
                        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
                        .text("Muy contento con la compra, el iPhone es muy rápido.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(1))
                        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
                        .text("El iPhone es muy bonito, la pantalla es increíble.")
                        .build(),
                    Commentary.builder()
                        .userId(USER_IDS.get(2))
                        .model(modelRepository.findByProductTitle("iPhone 13").get(0))
                        .text("No me ha gustado mucho, esperaba más.")
                        .build()));
			
			// Seed Likes
			likeRepository.saveAll(List.of(
					Like.builder()
						.userId(USER_IDS.get(0))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(1)).getFirst())
						.build(),
					Like.builder()
						.userId(USER_IDS.get(1))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(2)).getLast())
						.build(),
					Like.builder()
						.userId(USER_IDS.get(2))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(0)).getFirst())
						.build(),
					Like.builder()
						.userId(USER_IDS.get(0))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(2)).getFirst())
						.build(),
					Like.builder()
						.userId(USER_IDS.get(1))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(0)).getLast())
						.build(),
					Like.builder()
						.userId(USER_IDS.get(2))
						.commentary(commentaryRepository.findByUserId(USER_IDS.get(1)).getFirst())
						.build()
				));
		}
	}
}