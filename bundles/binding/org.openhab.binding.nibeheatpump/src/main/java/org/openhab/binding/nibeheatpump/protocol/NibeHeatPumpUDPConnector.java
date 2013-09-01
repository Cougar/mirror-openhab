/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.nibeheatpump.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for UDP communication.
 * 
 * Command for testing:
 * 
 * echo -e "\x5C\x00\x20\x68\x50\x01\xA8\x1F\x01\x00\xA8\x64\x00\xFD\xA7\xD0\x03\x44\x9C\x1E\x00\x4F\x9C\xA0\x00\x50\x9C\x78\x00\x51\x9C\x03\x01\x52\x9C\x1B\x01\x87\x9C\x14\x01\x4E\x9C\xC6\x01\x47\x9C\x01\x01\x15\xB9\xB0\xFF\x3A\xB9\x4B\x00\xC9\xAF\x00\x00\x48\x9C\x0D\x01\x4C\x9C\xE7\x00\x4B\x9C\x00\x00\xFF\xFF\x00\x00\xFF\xFF\x00\x00\xFF\xFF\x00\x00\x45" | nc -4u -w1 localhost 9999
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpUDPConnector extends NibeHeatPumpConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(NibeHeatPumpUDPConnector.class);

	int port = 9999;
	DatagramSocket socket = null;

	public NibeHeatPumpUDPConnector(int port) {

		logger.debug("Nibe heatpump UDP message listener started");
		this.port = port;
	}

	@Override
	public void connect() throws NibeHeatPumpException {
		
		if (socket == null) {
			try {
				socket = new DatagramSocket(port);
			} catch (SocketException e) {
				throw new NibeHeatPumpException(e);
			}
		}
		
	}

	@Override
	public void disconnect() throws NibeHeatPumpException {
		
		if (socket != null) {
			socket.close();
		}
	}

	@Override
	public byte[] receiveDatagram() throws NibeHeatPumpException {

		final int PACKETSIZE = 255;

		try {

			if (socket == null) {
				socket = new DatagramSocket(port);
			}
			
			// Create a packet
			DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE],
					PACKETSIZE);

			// Receive a packet (blocking)
			socket.receive(packet);

			return Arrays.copyOfRange(packet.getData(), 0,
					packet.getLength() - 1);

		} catch (SocketException e) {

			throw new NibeHeatPumpException(e);

		} catch (IOException e) {

			throw new NibeHeatPumpException(e);
		}

	}
}
