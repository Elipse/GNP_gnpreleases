package mx.com.eixy.gnp.customizador;

import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mx.com.eixy.gnp.gui.JEditor;
import mx.com.eixy.gnp.modelo.Reemplazo;

@Component
public class Adaptador {

	@Value("#{'${user.dir}' + '\\'}")
	private String userDirectory;

	@Value("#{'${directorioDeReglas}' + '\\'}")
	private String directorioDeReglas;
	
	@Autowired
	JEditor jEditor;

	Map<String, File> carpetas = new HashMap<>();
	Map<String, List<Reemplazo>> reemplazos = new HashMap<>();

	public File getRepositorio(String id) {
		return carpetas.get(id);
	}

	/**
	 * Carga los reemplazos de los repositorios hard a los repositorios soft.
	 * @throws IOException
	 */
	public void cargaReemplazos() throws IOException {

		Stream<Path> list = Files.list(Paths.get(directorioDeReglas));
		list.forEach(path -> {

			String json = null;

			File file = path.toFile();
			String fileName = path.getFileName().toString();

			carpetas.put(fileName, file);

			try {
				json = FileUtils.readFileToString(path.toFile(), Charset.forName("ISO-8859-1"));
			} catch (IOException e) {
				
			}

			reemplazos.put(fileName, deJsonAReemplazos(json));
		});

		list.close();
	}

	
	private List<Reemplazo> deJsonAReemplazos(String json) {
		Gson gson = new Gson();

		TypeToken<List<Reemplazo>> token = new TypeToken<List<Reemplazo>>() {
		};

		return gson.fromJson(json, token.getType());
	}

	
	public JDialog modificaRegla(String id) {
		jEditor.setModalityType(ModalityType.APPLICATION_MODAL);
		
		return jEditor;
	}
	

	public String buscaYreemplaza(String idCarpeta, String original) {

		String  adaptada = "";

		List<Reemplazo> lista = reemplazos.get(idCarpeta);
		
		for (Reemplazo reemplazo : lista) {
			String condicionBusqueda = reemplazo.getCondicionBusqueda();
			String cadenaActual = reemplazo.getCadenaActual();
			String cadenaNueva = reemplazo.getCadenaNueva();

			int inicioAnt = 0;

			Matcher m = Pattern.compile(condicionBusqueda).matcher(original);

			while (m.find()) {
				int inicio = m.start();
				int fin = m.end();
				adaptada += original.substring(inicioAnt, inicio);
				String seccion = original.substring(inicio, fin).replaceAll(cadenaActual, cadenaNueva);
				adaptada += seccion;
				inicioAnt = fin;
			}

			if (inicioAnt != 0) {
				original = adaptada + original.substring(inicioAnt);
				adaptada = "";
			}
		}
		return original;
	}

}
