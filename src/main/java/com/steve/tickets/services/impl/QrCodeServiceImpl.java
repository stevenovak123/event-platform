package com.steve.tickets.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.steve.tickets.Exceptions.QrCodeGenerationException;
import com.steve.tickets.Exceptions.QrCodeNotFoundException;
import com.steve.tickets.domain.entities.QrCode;
import com.steve.tickets.domain.entities.QrCodeStatusEnum;
import com.steve.tickets.domain.entities.Ticket;
import com.steve.tickets.repositories.QrCodeRepository;
import com.steve.tickets.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_height = 300;
    private static final int QR_width = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQRCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();

            String qrCodeImage = generateQRCodeImage(uniqueId);

            QrCode qrCode = new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setStatusEnum(QrCodeStatusEnum.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);
            return qrCodeRepository.saveAndFlush(qrCode);
        } catch (IOException | WriterException ex) {
            throw new QrCodeGenerationException("Unable to generate QR code", ex);
        }

    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode = qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId).orElseThrow(QrCodeNotFoundException::new);
        try {
            return Base64.getDecoder().decode(qrCode.getValue());
        } catch (IllegalArgumentException ex) {
            log.error("Invalid base64 QR code for ticketId: {}", ticketId, ex);
            throw new QrCodeNotFoundException();
        }

    }

    private String generateQRCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(uniqueId.toString(), BarcodeFormat.QR_CODE, QR_width, QR_height);

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}
