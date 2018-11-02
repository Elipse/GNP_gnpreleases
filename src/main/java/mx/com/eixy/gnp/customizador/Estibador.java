package mx.com.eixy.gnp.customizador;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.com.eixy.gnp.modelo.Member;
import mx.com.eixy.utilities.zos.ftp.FtpClientFactory;
import mx.com.eixy.utilities.zos.ftp.PDSConfig;
import mx.com.eixy.utilities.zos.ftp.ServerConfig;
import mx.com.eixy.utilities.zos.ftp.Transfer;

/**
 * Facade of mx.com.eixy.utilities.zos.ftp.*
 * 
 * @author ELIALVA
 *
 */

@Component
public class Estibador {

	@Resource(name = "mapaDeServidores")
	public Map<String, ServerConfig> configuraciones;

	@Autowired
	public PDSConfig pds;

	public List<String> servidores() {
		List<String> servidores = new ArrayList<>();
		servidores.addAll(configuraciones.keySet());
		return servidores;
	}

	/**
	 * Descarga el member de una biblioteca a un folder del file system local.
	 * 
	 * @param conexion
	 * @param biblioteca
	 * @param member
	 * @param folderLocal
	 * @return
	 * @throws IOException
	 */
	public File descarga(String server, String remoteFile, String localFile) throws IOException {

		FTPClient ftpClient = FtpClientFactory.fetchConnection(configuraciones.get(server));

		OutputStream localStream = Files.newOutputStream(Paths.get(localFile));

		Transfer.getFile(ftpClient, remoteFile, localStream);

		return new File(localFile);
	}

	/**
	 * <p>
	 * Carga un member en el servidor destino vía FTP.
	 * </p>
	 * 
	 * <pre>
	 * Ejemplo
	 * 	DNCQP.BTCH.JCLLIB(PGAQADCH) -> TN6EAM.PR1.BTCH.JCLLIB(IGAQADCH)
	 * 	Estibador.carga("INFOPR1", "TN6EAM.PR1.BTCH.JCLLIB", "PGAQADCH", "IGAQADCH", "temp/DISP/");
	 * 
	 * </pre>
	 * 
	 * @param conexion
	 *            El parámetro que indica el servidor FTP al que será transferido el
	 *            member.
	 * @param biblioteca
	 *            El nombre de la biblioteca que resguardará el member.
	 * @param memberOrigen
	 *            El nombre original del member y del archivo en el file-system
	 *            local.
	 * @param memberDestino
	 *            El nombre con el que quedará, el member, una vez que sea
	 *            transferido.
	 * @param folderLocal
	 *            La ruta que aloja el archivo que representa al member.
	 */
	public void carga(String server, String remoteFile, File localFile) throws IOException {

		FTPClient ftpClient = FtpClientFactory.fetchConnection(configuraciones.get(server));

		if (remoteFile.contains("(")) {
			String tmpRemoteFile = StringUtils.substringBefore(remoteFile, "(");
			if (!Transfer.exists(ftpClient, tmpRemoteFile)) {
				Transfer.createPDS(ftpClient, tmpRemoteFile, pds);
			}
		}

		InputStream localStream = Files.newInputStream(Paths.get(localFile.getAbsolutePath()));

		Transfer.putFile(ftpClient, remoteFile, localStream);
	}
}
