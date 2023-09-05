package org.wtech.concatgrouper.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wtech.concatgrouper.model.SC100;
import org.wtech.concatgrouper.model.SC150;

public class DataFetcher {

	private DatabaseService dbService =null;
	private static Logger logger = LoggerFactory.getLogger(DataFetcher.class);
	private DatabaseService getDBService() { 
		if(null == dbService) {
			dbService = new DatabaseService();
		}
		return dbService;
	}

	public List<SC100> fetchSC100( String accountNo ) throws SQLException {

		String tipoDoc = null;
		String nroDoc;
		String fechaEmision;
		String fecVenc;
		String montoDoc;
		String saldoActual;

		List<SC100> records = new ArrayList<>();

		String querySC100 = "SELECT \n"
				+ "TIPO_DOC, \n"
						+ "ABS(NRO_DOC) AS NRO_DOC, \n"
						+ "FECHA_EMISION, \n"
						+ "FEC_VENC, \n"
						+ "ABS(MONTO_DOC) AS MONTO_DOC, \n"
						+ "ABS(SALDO_ACTUAL) AS SALDO_ACTUAL \n"
						+ "FROM NKADM.KN_CI_SALDO_SAP_NEW \n"
						+ "WHERE TRIM(ACCOUNT_NO) = ?\n";




		try(Connection conn = getDBService().getConnection()) {
			PreparedStatement stmtSC100 = conn.prepareStatement(querySC100);
			stmtSC100.setString(1, accountNo); 

			logger.debug("Query for SC100: "+ querySC100+ " Account_no: "+accountNo);
			ResultSet rsSC100 = stmtSC100.executeQuery();

			logger.debug("stmtSC100: "+stmtSC100.toString());
			logger.debug("rsSC100: "+rsSC100.toString());
			if (!rsSC100.isBeforeFirst()) {
				logger.debug("No data found in rsSC100 for Account_no: " + accountNo);
			}

			while (rsSC100.next()) {
				tipoDoc = rsSC100.getString("TIPO_DOC");
				nroDoc = rsSC100.getString("NRO_DOC");
				fechaEmision = rsSC100.getString("FECHA_EMISION");
				fecVenc = rsSC100.getString("FEC_VENC");
				montoDoc = rsSC100.getString("MONTO_DOC");
				saldoActual = rsSC100.getString("SALDO_ACTUAL");

				String decodedDocType = "";
				switch (tipoDoc) {
				case "FA":
				case "FE":
					decodedDocType = "FACTURA   ";
					break;
				case "BA":
				case "BE":
					decodedDocType = "BOLETA    ";
					break;
				default:
					decodedDocType = "FACTURA   ";
					break;
				}
				
				int nroDocInt = Integer.parseInt(nroDoc);
				int montoDocInt = Integer.parseInt(montoDoc);
				int saldoActualInt = Integer.parseInt(saldoActual);
				
				String paddedNroDoc = String.format("%012d", nroDocInt);
				String paddedMontoDoc = String.format("%012d", montoDocInt);
				String paddedSaldoActual = String.format("%012d", saldoActualInt);

				records.add( new SC100(decodedDocType, paddedNroDoc, fechaEmision, fecVenc, paddedMontoDoc, paddedSaldoActual));

			}
			rsSC100.close();
			stmtSC100.close();

		} catch (SQLException e) {
			logger.error("SQL Error Code: " + e.getErrorCode());
			logger.error("SQL State: " + e.getSQLState());
			logger.error("Error Message: " + e.getMessage());
			e.printStackTrace();
		}



		return records;
	}

	public SC150 fetchSC150(String accountNo) throws SQLException {
		long totalMontoDoc = 0; 
		long totalSaldoActual = 0;

		String querySC150 = "SELECT "
				+ "SUM(ABS(MONTO_DOC)) AS TOTAL_MONTO_DOC, "
				+ "SUM(ABS(SALDO_ACTUAL)) AS TOTAL_SALDO_ACTUAL "
				+ "FROM NKADM.KN_CI_SALDO_SAP_NEW "
				+ "WHERE TRIM(ACCOUNT_NO) = ?";


		try (Connection connection = getDBService().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(querySC150)) {

			// Set the parameters
			preparedStatement.setString(1, accountNo);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					totalMontoDoc = resultSet.getInt("TOTAL_MONTO_DOC");
					totalSaldoActual = resultSet.getInt("TOTAL_SALDO_ACTUAL");
				}
			}
		}

		String paddedMontoDoc = String.format("%012d", totalMontoDoc);
		String paddedSaldoActual = String.format("%012d", totalSaldoActual);

		return  new SC150(paddedMontoDoc, paddedSaldoActual);
	}
}
