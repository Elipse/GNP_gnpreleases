package mx.com.eixy.gnpreleases;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import mx.com.eixy.utilities.zos.ftp.PDSConfig;
import mx.com.eixy.utilities.zos.ftp.ServerConfig;

@Configuration
@ComponentScan(basePackages = { "mx.com.eixy.gnp" })
@PropertySources({ @PropertySource("file:${user.dir}/app.properties") })
public class CustomConfig {

	// private static final String FOLDER =
	// CustomConfig.class.getClassLoader().getResource("").getPath().replace("%20","
	// ");
	private static final String FOLDER = System.getProperty("user.dir");

	public static final String ARCHIVO_SERVIDORES_FTP = FOLDER + "/_jsons/servers.json";
	public static final String ARCHIVO_PDS = FOLDER + "/_jsons/pds.json";
	public static final String ARCHIVO_TIPOS_FTP = FOLDER + "/_jsons/tipos.json";
	public static final String ARCHIVO_ICON_PROCESO = FOLDER + "/_icons/process_on_32.png";
	public static final String FOLDER_TMP = FOLDER + "/_temp/";

	@Value("${mongodb.url}")
	private String mongodbUrl;

	@Autowired
	private Environment environment;

	// This method to resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public String folderDeReglas() {
		String s = environment.getProperty("folder.reemplazos");
		return FOLDER + "/" + s;
	}

	@Bean
	public String newServer() {
		String mongodbUrl2 = environment.getProperty("mongodb.url");
		System.out.println("--- " + System.getProperty("user.dir"));
		System.out.println("Lista de servidores creada --- " + mongodbUrl + " vs " + mongodbUrl2);
		return "";
	}

	@Bean
	public PDSConfig pds() {

		PDSConfig pds = null;

		Gson gson = new Gson();
		try {
			pds = gson.fromJson(new FileReader(ARCHIVO_PDS), PDSConfig.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("el pds " + pds);

		return pds;
	}

	@Bean
	public Map<String, ServerConfig> mapaDeServidores() {

		Map<String, ServerConfig> mapa = new HashMap<>();
		Gson gson = new Gson();

		TypeToken<List<ServerConfig>> token = new TypeToken<List<ServerConfig>>() {
		};

		try {
			List<ServerConfig> lista = gson.fromJson(new FileReader(ARCHIVO_SERVIDORES_FTP), token.getType());
			lista.stream().forEach(elemento -> {
				mapa.put(elemento.getServerName(), elemento);
			});

		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}

		return mapa;
	}

	@Bean
	public List<String> tiposDeElementos() {

		List<String> lista = new ArrayList<>();
		try {
			Gson gson = new Gson();
			TypeToken<List<String>> token2 = new TypeToken<List<String>>() {
			};

			lista = gson.fromJson(new FileReader(ARCHIVO_TIPOS_FTP), token2.getType());
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}

		return lista;

	}

	@Bean
	public ImageIcon iconProceso() {
		File proceso = new File(ARCHIVO_ICON_PROCESO);
		BufferedImage image = null;
		try {

			image = ImageIO.read(proceso);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ImageIcon(image);
	}
}
