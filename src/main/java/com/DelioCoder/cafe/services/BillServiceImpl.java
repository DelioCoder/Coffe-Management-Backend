package com.DelioCoder.cafe.services;

import com.DelioCoder.cafe.POJO.Bill;
import com.DelioCoder.cafe.config.JWT.JwtFilter;
import com.DelioCoder.cafe.constant.CoffeConstants;
import com.DelioCoder.cafe.dao.BillDao;
import com.DelioCoder.cafe.interfaces.Bill.BillService;
import com.DelioCoder.cafe.utils.CoffeUtils;
import com.DelioCoder.cafe.utils.pdf.MyTable;
import com.DelioCoder.cafe.utils.pdf.MyTextClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Slf4j
@Service
public class BillServiceImpl implements BillService
{
    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap)
    {

        try {

            String fileName;

            if(validateRequestMap(requestMap))
            {

                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate"))
                {
                    fileName = (String) requestMap.get("uuid");
                }else {
                    fileName = CoffeUtils.getUUID();

                    requestMap.put("uuid", fileName);

                    insertBill(requestMap);
                }

                JSONArray jsonArray = CoffeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));

                PDDocument document = new PDDocument();
                PDRectangle pageSize = PDRectangle.A4;
                PDPage page = new PDPage(pageSize);
                document.addPage(page);

                int pageHeight = (int) pageSize.getHeight();
                int pageWidth = (int) pageSize.getWidth();
                int[] cellWidths = { 150, 110, 100, 90, 90 };
                Color tableHeadColor = new Color(240, 93, 11);
                Color tableBodyColor = new Color(219, 218, 198);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                MyTextClass myTextClass = new MyTextClass(document, contentStream);
                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                MyTable myTable = new MyTable(document, contentStream);
                PDImageXObject headImage = PDImageXObject.createFromFile("src/main/resources/static/images/Annie.png", document);

                addImageToHeader(contentStream, headImage, pageHeight, pageWidth);
                addTextToBody(myTextClass, requestMap, pageHeight, font);
                createTableHeader(myTable, cellWidths, pageHeight, font, tableHeadColor);
                addRowToTable(jsonArray, myTable, tableBodyColor);
                createPDF(contentStream, document, fileName);

                return new ResponseEntity<>("uuid: " + fileName, HttpStatus.OK);

            }else {
                return CoffeUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex)
        {
            log.error("BillServiceImp - Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {

        log.info("Inside getPdf: requestMap {}", requestMap);

        try {

            byte[] byteArray = new byte[0];

            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
            {
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);

            }else {
                String filePath = CoffeConstants.STORE_LOCATION+"\\"+(String) requestMap.get("uuid") + ".pdf";

                if (CoffeUtils.isFileExist(filePath))
                {
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                }else {
                    requestMap.put("isGenerate", false);
                    generateReport(requestMap);
                    byteArray = getByteArray(filePath);
                    return new ResponseEntity<>(byteArray, HttpStatus.OK);
                }
            }

        }catch (Exception ex){
            log.error("BillService - Error: ", ex);
        }

        return null;

    }

    private byte[] getByteArray(String filePath) throws IOException {

        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;

    }

    @Override
    public ResponseEntity<List<Bill>> retrieveBills() {

        List<Bill> list = new ArrayList<>();

        try {

            if (jwtFilter.isAdmin())
            {

                list = billDao.getAllBills();

            }else {
                list = billDao.getBillByUsername(jwtFilter.getCurrentUser());
            }

        }catch (Exception ex)
        {
            log.error("RetrieveBills - Error: ", ex);
        }

        return new ResponseEntity<>(list, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {

        try {

            Optional<Bill> optional = billDao.findById(id);

            if (optional.isPresent())
            {
                billDao.deleteById(id);
                return CoffeUtils.getResponseEntity("Bill Deleted Successfully", HttpStatus.OK);
            }else {
                return CoffeUtils.getResponseEntity("Bill id does not exist", HttpStatus.OK);
            }

        }catch (Exception ex)
        {
            log.error("BillServiceImp - Error: " + ex);
        }

        return CoffeUtils.getResponseEntity(CoffeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private void insertBill(Map<String, Object> requestMap) {

        try
        {

            Bill bill = new Bill();

            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber(Integer.parseInt((String) requestMap.get("contactNumber")));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Double.parseDouble((String) requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());

            billDao.save(bill);

        }catch (Exception ex) {
            log.error("InsertBill - Error: " + ex);
        }

    }

    private boolean validateRequestMap(Map<String, Object> requestMap)
    {

        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");

    }
    
    private void addImageToHeader(PDPageContentStream contentStream, PDImageXObject headImage, int pageHeight, int pageWidth) throws IOException {
        contentStream.drawImage(headImage, 0, pageHeight-235, pageWidth, 239);
    }

    private void addTextToBody(MyTextClass myTextClass, Map<String, Object> requestMap, int pageHeight, PDFont font) throws IOException {

        myTextClass.addSingleLineText("Ornn's Coffe", 0, pageHeight - 40, font, 40, Color.BLACK);

        myTextClass.addSingleLineText("Name: " + requestMap.get("name"), 25, pageHeight - 250, font, 16, Color.BLACK);
        myTextClass.addSingleLineText("Contact Number: " + requestMap.get("contactNumber"), 25, pageHeight - 270, font, 16, Color.BLACK);
        myTextClass.addSingleLineText("Email: " + requestMap.get("email"), 25, pageHeight - 290, font, 16, Color.BLACK);
        myTextClass.addSingleLineText("paymentMethod: " + requestMap.get("paymentMethod"), 25, pageHeight - 310, font, 16, Color.BLACK);
    }

    private void createTableHeader(MyTable myTable, int[] cellWidths, int pageHeight, PDFont font, Color tableHeadColor) throws IOException {
        myTable.setTable(cellWidths, 40, 25, pageHeight - 330);
        myTable.setTableFont(font, 13, Color.BLACK);

        myTable.addCell("Name", tableHeadColor);
        myTable.addCell("Category", tableHeadColor);
        myTable.addCell("Quantity", tableHeadColor);
        myTable.addCell("Price", tableHeadColor);
        myTable.addCell("Total", tableHeadColor);
    }

    private void addRowToTable(JSONArray jsonArray, MyTable myTable, Color tableBodyColor) throws IOException {
        for (Object item : jsonArray)
        {
            JSONObject product = (JSONObject) item;

            myTable.addCell(String.valueOf(product.getString("name")), tableBodyColor);
            myTable.addCell(String.valueOf(product.getString("category")), tableBodyColor);
            myTable.addCell(String.valueOf(product.getString("quantity")), tableBodyColor);
            myTable.addCell(String.valueOf(product.getDouble("price")), tableBodyColor);
            myTable.addCell(String.valueOf(product.getDouble("total")), tableBodyColor);
        }
    }

    private void createPDF(PDPageContentStream contentStream, PDDocument document, String fileName) throws IOException {
        contentStream.close();
        document.save(CoffeConstants.STORE_LOCATION + "\\" + fileName + ".pdf");
        document.close();

        System.out.println("Documento PDF con tabla creada exitosamente.");
    }

}
