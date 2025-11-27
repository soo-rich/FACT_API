package com.soosmart.facts.utils;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;

    public void sendDefaultPasswordMail(String to, String password, String username) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("🔐 Bienvenue - Vos identifiants de connexion");
            
            String htmlContent = buildWelcomeEmailTemplate(username, password);
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }
    public void sendForgotPasswordEmail(String to, String password) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("🔐 Réinitialisation de mot de passe");
            
            String htmlContent = buildDefaultPaaswordForgot(to, password);
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage(), e);
        }
    }

    private String buildWelcomeEmailTemplate(String username, String password) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .container {
                            background: linear-gradient(to bottom, #ffffff, #f8f9fa);
                            border-radius: 10px;
                            padding: 30px;
                            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                        }
                        .header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #007bff;
                            margin-bottom: 30px;
                        }
                        .header h1 {
                            color: #007bff;
                            margin: 0;
                            font-size: 24px;
                        }
                        .credentials-box {
                            background: #f8f9fa;
                            border-left: 4px solid #007bff;
                            padding: 20px;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .credential-item {
                            margin: 15px 0;
                        }
                        .credential-label {
                            font-weight: bold;
                            color: #495057;
                            display: block;
                            margin-bottom: 5px;
                        }
                        .credential-value {
                            font-family: 'Courier New', monospace;
                            background: white;
                            padding: 10px;
                            border-radius: 4px;
                            font-size: 16px;
                            color: #212529;
                            border: 1px solid #dee2e6;
                        }
                        .warning-box {
                            background: #fff3cd;
                            border-left: 4px solid #ffc107;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .warning-box strong {
                            color: #856404;
                        }
                        .steps {
                            margin: 20px 0;
                        }
                        .steps ol {
                            padding-left: 20px;
                        }
                        .steps li {
                            margin: 10px 0;
                            line-height: 1.8;
                        }
                        .footer {
                            margin-top: 30px;
                            padding-top: 20px;
                            border-top: 1px solid #dee2e6;
                            text-align: center;
                            color: #6c757d;
                            font-size: 14px;
                        }
                        .icon {
                            font-size: 20px;
                            margin-right: 5px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🎉 Bienvenue sur FACTS</h1>
                            <p style="color: #6c757d; margin-top: 10px;">Votre compte a été créé avec succès</p>
                        </div>
                        
                        <p>Bonjour <strong>%s</strong>,</p>
                        
                        <p>Nous sommes ravis de vous accueillir ! Votre compte a été créé et vous pouvez dès maintenant accéder à la plateforme avec les identifiants ci-dessous :</p>
                        
                        <div class="credentials-box">
                            <div class="credential-item">
                                <span class="credential-label">👤 Nom d'utilisateur :</span>
                                <div class="credential-value">%s</div>
                            </div>
                            <div class="credential-item">
                                <span class="credential-label">🔑 Mot de passe temporaire :</span>
                                <div class="credential-value">%s</div>
                            </div>
                        </div>
                        
                        <div class="warning-box">
                            <strong>⚠️ Important - Sécurité</strong>
                            <p style="margin: 10px 0 0 0;">Pour des raisons de sécurité, nous vous recommandons fortement de changer ce mot de passe temporaire lors de votre première connexion.</p>
                        </div>
                        
                        <div class="steps">
                            <strong>📋 Premiers pas :</strong>
                            <ol>
                                <li>Connectez-vous à la plateforme avec vos identifiants</li>
                                <li>Accédez à votre profil</li>
                                <li>Modifiez votre mot de passe par un mot de passe personnel et sécurisé</li>
                                <li>Complétez les informations de votre profil si nécessaire</li>
                            </ol>
                        </div>
                        
                        <p style="margin-top: 30px;">Si vous rencontrez des difficultés ou si vous avez des questions, n'hésitez pas à contacter notre équipe support.</p>
                        
                        <div class="footer">
                            <p><strong>L'équipe FACTS</strong></p>
                            <p style="font-size: 12px; color: #adb5bd; margin-top: 10px;">
                                Cet email a été envoyé automatiquement, merci de ne pas y répondre.<br>
                                Si vous n'avez pas demandé la création de ce compte, veuillez nous contacter immédiatement.
                            </p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(username, username, password);
    }


    private String buildDefaultPaaswordForgot(String email, String password){
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .container {
                            background: linear-gradient(to bottom, #ffffff, #f8f9fa);
                            border-radius: 10px;
                            padding: 30px;
                            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                        }
                        .header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 2px solid #dc3545;
                            margin-bottom: 30px;
                        }
                        .header h1 {
                            color: #dc3545;
                            margin: 0;
                            font-size: 24px;
                        }
                        .credentials-box {
                            background: #f8f9fa;
                            border-left: 4px solid #dc3545;
                            padding: 20px;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .credential-item {
                            margin: 15px 0;
                        }
                        .credential-label {
                            font-weight: bold;
                            color: #495057;
                            display: block;
                            margin-bottom: 5px;
                        }
                        .credential-value {
                            font-family: 'Courier New', monospace;
                            background: white;
                            padding: 10px;
                            border-radius: 4px;
                            font-size: 16px;
                            color: #212529;
                            border: 1px solid #dee2e6;
                        }
                        .alert-box {
                            background: #f8d7da;
                            border-left: 4px solid #dc3545;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .alert-box strong {
                            color: #721c24;
                        }
                        .security-box {
                            background: #fff3cd;
                            border-left: 4px solid #ffc107;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 5px;
                        }
                        .security-box strong {
                            color: #856404;
                        }
                        .steps {
                            margin: 20px 0;
                        }
                        .steps ol {
                            padding-left: 20px;
                        }
                        .steps li {
                            margin: 10px 0;
                            line-height: 1.8;
                        }
                        .footer {
                            margin-top: 30px;
                            padding-top: 20px;
                            border-top: 1px solid #dee2e6;
                            text-align: center;
                            color: #6c757d;
                            font-size: 14px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 30px;
                            background-color: #dc3545;
                            color: white !important;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 20px 0;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🔒 Réinitialisation de mot de passe</h1>
                            <p style="color: #6c757d; margin-top: 10px;">Votre mot de passe a été réinitialisé</p>
                        </div>
                        
                        <p>Bonjour,</p>
                        
                        <p>Nous avons reçu une demande de réinitialisation de mot de passe pour votre compte FACTS. Votre mot de passe a été réinitialisé avec succès.</p>
                        
                        <div class="credentials-box">
                            <div class="credential-item">
                                <span class="credential-label">📧 Email de connexion :</span>
                                <div class="credential-value">%s</div>
                            </div>
                            <div class="credential-item">
                                <span class="credential-label">🔑 Nouveau mot de passe temporaire :</span>
                                <div class="credential-value">%s</div>
                            </div>
                        </div>
                        
                        <div class="alert-box">
                            <strong>🚨 Attention - Action requise</strong>
                            <p style="margin: 10px 0 0 0;">Ce mot de passe est <strong>temporaire</strong> et doit être changé immédiatement après votre première connexion.</p>
                        </div>
                        
                        <div class="security-box">
                            <strong>🔐 Conseils de sécurité</strong>
                            <ul style="margin: 10px 0 0 0; padding-left: 20px;">
                                <li>Choisissez un mot de passe complexe (minimum 8 caractères)</li>
                                <li>Utilisez des majuscules, minuscules, chiffres et caractères spéciaux</li>
                                <li>Ne partagez jamais votre mot de passe</li>
                                <li>N'utilisez pas le même mot de passe sur plusieurs sites</li>
                            </ul>
                        </div>
                        
                        <div class="steps">
                            <strong>📋 Étapes à suivre :</strong>
                            <ol>
                                <li>Connectez-vous à FACTS avec le mot de passe temporaire ci-dessus</li>
                                <li>Accédez immédiatement à votre profil</li>
                                <li>Cliquez sur "Changer le mot de passe"</li>
                                <li>Créez un nouveau mot de passe fort et sécurisé</li>
                                <li>Confirmez et enregistrez le nouveau mot de passe</li>
                            </ol>
                        </div>
                        
                        <div class="alert-box">
                            <strong>⚠️ Vous n'avez pas demandé cette réinitialisation ?</strong>
                            <p style="margin: 10px 0 0 0;">Si vous n'êtes pas à l'origine de cette demande, veuillez contacter immédiatement notre équipe support. Votre compte pourrait être compromis.</p>
                        </div>
                        
                        <p style="margin-top: 30px;">Pour toute question ou assistance, notre équipe support est à votre disposition.</p>
                        
                        <div class="footer">
                            <p><strong>L'équipe FACTS</strong></p>
                            <p style="font-size: 12px; color: #adb5bd; margin-top: 10px;">
                                Cet email a été envoyé automatiquement, merci de ne pas y répondre.<br>
                                Pour des raisons de sécurité, ce mot de passe temporaire expire dans 24 heures.<br>
                                Si vous n'avez pas demandé cette réinitialisation, contactez-nous immédiatement.
                            </p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(email, password);
    }
}
