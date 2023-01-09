package com.mindhub.homebanking.util;

import java.util.Calendar;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class Pdf {
    public void convertTO(ClientDTO clientDTO, AccountDTO accountDTO ) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // logo image
        PDImageXObject image = PDImageXObject.createFromFile("/src/main/resources/static/web/img/LOGO_CON_TEXTO.png", document);
        contentStream.drawImage(image, 50, 780, 100, 60);
        // image watermark
        PDImageXObject image2 = PDImageXObject.createFromFile("/src/main/resources/static/web/img/INDEX_MARCA_AGUA.png", document);
        contentStream.drawImage(image2, 200, 300, 200, 200);

        // title
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 26);
        contentStream.newLineAtOffset(180, 780);
        contentStream.showText("ANTARTIDA BANK");
        contentStream.endText();

        //subtitle
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 18);
        contentStream.newLineAtOffset(50, 760);
        contentStream.showText("Record of customer transactions: "+ clientDTO.getFirstName() + " " + clientDTO.getLastName());
        contentStream.endText();

        // account
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 18);
        contentStream.newLineAtOffset(50, 740);
        contentStream.showText("Account: " + accountDTO.getNumber());
        contentStream.endText();

        // line
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 18);
        contentStream.newLineAtOffset(50, 730);
        contentStream.showText("__________________________________________________");
        contentStream.endText();

        // -------bank information---------
        // get day, month and year
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        // create string "dd/mm/yyyy"
        String dateString = String.format(" %02d/%02d/%04d", day, month, year);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 7);
        contentStream.newLineAtOffset(50, 715);
        contentStream.showText("These instructions have been prepared in accordance with banking regulations and current instructions:  " + dateString);
        contentStream.endText();

        //Body
        this.body(contentStream, page, accountDTO.getTransactions() );

        contentStream.close();

        document.save("/src/main/resources/static/web/cartolas/cartola_" + clientDTO.getFirstName() +"_"+ accountDTO.getNumber()+".pdf");
        System.out.println("PDF created");
        document.close();

    }

    //body method
    private void body (PDPageContentStream contentStream1, PDPage page, Set<TransactionDTO> transactionDTOList) throws IOException {

        int offset = 700;
        for (TransactionDTO transactionDTO : transactionDTOList ) {
            offset -= 20;
            contentStream1.beginText();
            contentStream1.setFont(PDType1Font.HELVETICA, 14);
            contentStream1.newLineAtOffset(50, offset);
            contentStream1.showText(
                              transactionDTO.getDescription() + " | "
                            + transactionDTO.getDate().toLocalDate().toString() + " | "
                            + transactionDTO.getType().toString() + " | "
                            + String.valueOf(transactionDTO.getAmount()));
            contentStream1.endText();
        }
    }
}


