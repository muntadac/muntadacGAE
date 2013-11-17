package com.dakidev.muntadacGAE;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

@SuppressWarnings("serial")
public class MuntadacGAEServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DateTime dt;

		int day;
		int month;
		int year;
		String dateType;
		String gender;

		try {

			day = Integer.parseInt(req.getParameter("day").replaceAll(" ", ""));

			month = Integer.parseInt(req.getParameter("month").replaceAll(" ",""));

			year = Integer.parseInt(req.getParameter("year").replaceAll(" ", ""));
			
			dateType = req.getParameter("type_date");
			
			gender = req.getParameter("gender");

			dt = new DateTime(year, month, day, 12, 0, 0, 0);

			if (dateType.equals("hijri")) {

				dt = new DateTime(year, month, day, 12, 0, 0, 0,IslamicChronology.getInstanceUTC());

				dt = dt.withChronology(ISOChronology.getInstanceUTC());

			}

			System.out.println("** " + dt.toDate());

			traiterExcel(dt, req, gender);

		} catch (Exception e) {

			req.setAttribute("errorMessage","المرجو التأكد من التاريخ");
		}

		

		// get back to order.jsp page using forward
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}

	private void traiterExcel(DateTime dt, HttpServletRequest req, String gender) {

		HSSFSheet my_worksheet = null;

		InputStream input_document = null;

	//	FileOutputStream out = null;

		HSSFWorkbook my_xls_workbook = null;

		try {

			input_document = getServletContext().getResourceAsStream("/WEB-INF/dateFile.xls");

			my_xls_workbook = new HSSFWorkbook(input_document);

			my_worksheet = my_xls_workbook.getSheetAt(0);

			boolean inInterval = false;

			for (int i = 0; i <= my_worksheet.getLastRowNum(); i++) {

				if (my_worksheet.getRow(i).getCell(0) != null) {

					String dateExcel = my_worksheet.getRow(i).getCell(0).getStringCellValue();

					dateExcel = dateExcel.replaceAll(" ", "");

					// System.out.println(dateExcel);

					String dateDebutString = dateExcel.substring(0,	dateExcel.indexOf("-"));

					String dateFinString = dateExcel.substring(dateExcel.indexOf("-") + 1, dateExcel.length());

					String[] dateDebutarray = dateDebutString.split("/");

					String[] dateFinarray = dateFinString.split("/");

					DateTime dtDebut = new DateTime(
							Integer.parseInt(dateDebutarray[0]),
							Integer.parseInt(dateDebutarray[1]),
							Integer.parseInt(dateDebutarray[2]), 23, 0, 0, 0);

					DateTime dtFin = new DateTime(
							Integer.parseInt(dateFinarray[0]),
							Integer.parseInt(dateFinarray[1]),
							Integer.parseInt(dateFinarray[2]), 01, 0, 0, 0);

					if (dtFin.toDate().getTime() < dtDebut.toDate().getTime()) {

						DateTime dtTemp = dtFin.plusMillis(1);

						dtFin = dtDebut.plusMillis(1);

						dtDebut = dtTemp.plusMillis(1);

					}

					System.out.println(dtDebut.toDate() + "    "
							+ dtFin.toDate());

					if ((dt.isAfter(dtDebut) || dt.isEqual(dtDebut)) && (dt.isBefore(dtFin) || dt.isEqual(dtFin))) {

						if (my_worksheet.getRow(i).getCell(0) != null) {

							req.setAttribute("text1", my_worksheet.getRow(i).getCell(1).getStringCellValue());

							inInterval = true;

							break;

						}

					}
					// System.out.println(dtDebut.toDate().toString() + "  " +
					// dtFin.toDate().toString());
				}

			}

			if (!inInterval) {

				req.setAttribute("text1", "not in all intervals");

			}

			int n = -1;

			if ((dt.isAfter(new DateTime(1926, 10, 15, 0, 0)) && dt
					.isBefore(new DateTime(1938, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(1950, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(1962, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(1974, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(1986, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(1998, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(2010, 10, 14, 0, 0)))) {

				if (gender.equals("male")) {

					n = 22 + dt.getMonthOfYear();

				} else {

					n = 35 - dt.getMonthOfYear();
				}

			} else if ((dt.isAfter(new DateTime(1938, 10, 15, 0, 0)) && dt
					.isBefore(new DateTime(1950, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(1962, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(1974, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(1986, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(1998, 10, 14, 0, 0)))
					|| (dt.isAfter(new DateTime(2010, 10, 15, 0, 0)) && dt
							.isBefore(new DateTime(2015, 10, 14, 0, 0)))) {

				if (gender.equals("male")) {

					n = 34 + dt.getMonthOfYear();

				} else {

					n = 47 - dt.getMonthOfYear();
				}

			}

			if (n == -1) {

				req.setAttribute("text2", "not in all intervals");

			} else {

				if (my_worksheet.getRow(n).getCell(2) != null) {

					req.setAttribute("text2", my_worksheet.getRow(n + 1).getCell(2).getStringCellValue());

				} else {

					req.setAttribute("text2", "not in all intervals");
				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			try {
				input_document.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
