package com.example.Lambda.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateFactory {

    @Autowired
    private JwtService jwtService;

    private final String lambdaUrl;

    public EmailTemplateFactory() {
        String tempUrl = System.getenv("LAMBDA_URL");
        if (tempUrl == null || tempUrl.isBlank()) {
            try {
                io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure().load();
                tempUrl = dotenv.get("LAMBDA_URL");
            } catch (Exception e) {
                throw new RuntimeException("LAMBDA_URL not set in environment or .env file");
            }
        }
        this.lambdaUrl = tempUrl;
    }


    public String getTemplate(EmailRequest request) {
        return switch (request.getType().toUpperCase()) {
            case "PASSWORD_RECOVERY" -> {
                String token = jwtService.generateToken(request.getTo());
                yield passwordRecoveryTemplate(token);
            }
            case "RATE_US" -> {
                String token = jwtService.generateToken(request.getTo());
                yield rateUsTemplate(token);
            }
            default -> throw new IllegalArgumentException("Invalid email type: " + request.getType());
        };
    }

    private String passwordRecoveryTemplate(String token) {
        String recoveryUrl = lambdaUrl + "/reset-password?token=" + token;
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html dir="ltr" lang="es">
                <head>
                    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
                    <meta name="x-apple-disable-message-reformatting" />
                </head>
                <body style='background-color:rgb(243,244,246);font-family:ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";padding-top:40px;padding-bottom:40px'>
                    <!--$-->
                    <div style="display:none;overflow:hidden;line-height:1px;opacity:0;max-height:0;max-width:0">
                    Restablece tu contraseña
                    </div>
                    <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="margin-left:auto;margin-right:auto;background-color:rgb(255,255,255);border-radius:8px;padding:48px;max-width:600px">
                    <tbody>
                        <tr style="width:100%%">
                        <td>
                            <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation">
                            <tbody>
                                <tr>
                                <td>
                                    <h1 style="font-size:24px;font-weight:700;color:rgb(255,107,0);margin:0px;margin-bottom:24px">
                                    Solicitud de Restablecimiento de Contraseña
                                    </h1>
                                    <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:24px;margin-top:16px">
                                    Hola,
                                    </p>
                                    <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:24px;margin-top:16px">
                                    Hemos recibido una solicitud para restablecer tu contraseña. Para crear una nueva contraseña, haz clic en el botón a continuación:
                                    </p>
                                    <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="text-align:center;margin-top:32px;margin-bottom:32px">
                                    <tbody>
                                        <tr>
                                        <td>
                                            <a href="%s" style="background-color:rgb(255,107,0);color:rgb(255,255,255);font-weight:700;padding-top:12px;padding-bottom:12px;padding-left:24px;padding-right:24px;border-radius:4px;text-decoration-line:none;text-align:center;box-sizing:border-box;line-height:100%%;text-decoration:none;display:inline-block;max-width:100%%;mso-padding-alt:0px;padding:12px 24px 12px 24px" target="_blank">
                                            <span style="max-width:100%%;display:inline-block;line-height:120%%;mso-padding-alt:0px;mso-text-raise:9px">Restablecer Contraseña</span>
                                            </a>
                                        </td>
                                        </tr>
                                    </tbody>
                                    </table>
                                    <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:24px;margin-top:16px">
                                    Este enlace para restablecer la contraseña caducará en 4 horas. Si no solicitaste un restablecimiento de contraseña, puedes ignorar este correo electrónico.
                                    </p>
                                    <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:8px;margin-top:16px">
                                    Gracias,
                                    </p>
                                    <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);font-weight:700;margin-bottom:16px;margin-top:16px">
                                    El Equipo de U-nidos
                                    </p>
                                </td>
                                </tr>
                            </tbody>
                            </table>
                            <hr style="border-color:rgb(229,231,235);margin-top:32px;margin-bottom:32px;width:100%%;border:none;border-top:1px solid #eaeaea" />
                            <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation">
                            <tbody>
                                <tr>
                                <td>
                                    <p style="font-size:14px;color:rgb(107,114,128);margin-bottom:8px;line-height:24px;margin-top:16px">
                                    Si tienes problemas para hacer clic en el botón, copia y pega la siguiente URL en tu navegador web:
                                    </p>
                                    <a href="%s" style="font-size:14px;color:rgb(255,107,0);word-break:break-all;text-decoration-line:none" target="_blank">%s</a>
                                </td>
                                </tr>
                            </tbody>
                            </table>
                        </td>
                        </tr>
                    </tbody>
                    </table>
                    <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="margin-left:auto;margin-right:auto;margin-top:32px;max-width:600px">
                    <tbody>
                        <tr style="width:100%%">
                        <td>
                            <p style="text-align:center;font-size:12px;color:rgb(107,114,128);margin:0px;line-height:24px;margin-bottom:0px;margin-top:0px;margin-left:0px;margin-right:0px">
                            © 2025 U-nidos S.A. Todos los derechos reservados.
                            </p>
                            <p style="text-align:center;font-size:12px;color:rgb(107,114,128);margin:0px;line-height:24px;margin-bottom:0px;margin-top:0px;margin-left:0px;margin-right:0px">
                            Bogotá, Colombia
                            </p>
                        </td>
                        </tr>
                    </tbody>
                    </table>
                    <!--7--><!--/$-->
                </body>
                </html>
                """.formatted(recoveryUrl, recoveryUrl, recoveryUrl);
    }

    private String rateUsTemplate(String token) {
    String rateUrl = lambdaUrl + "/student-satisfaction?token=" + token;
    return String.format("""
        <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
        <html dir="ltr" lang="es">
        <head>
            <meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
            <meta name="x-apple-disable-message-reformatting" />
        </head>
        <body style='background-color:rgb(243,244,246);font-family:ui-sans-serif, system-ui, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";padding-top:40px;padding-bottom:40px'>
            <div style="display:none;overflow:hidden;line-height:1px;opacity:0;max-height:0;max-width:0">
                ¡Califícanos!
            </div>
            <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="margin-left:auto;margin-right:auto;background-color:rgb(255,255,255);border-radius:8px;padding:48px;max-width:600px">
                <tbody>
                    <tr style="width:100%%">
                        <td>
                            <h1 style="font-size:24px;font-weight:700;color:rgb(255,107,0);margin:0px;margin-bottom:24px">
                                ¿Cómo fue tu experiencia con nosotros?
                            </h1>
                            <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:24px;margin-top:16px">
                                Tu opinión es muy importante para nosotros. Ayúdanos a mejorar dejando una calificación de tu experiencia.
                            </p>
                            <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="text-align:center;margin-top:32px;margin-bottom:32px">
                                <tbody>
                                    <tr>
                                        <td>
                                            <a href="%s" style="background-color:rgb(255,107,0);color:rgb(255,255,255);font-weight:700;padding-top:12px;padding-bottom:12px;padding-left:24px;padding-right:24px;border-radius:4px;text-decoration-line:none;text-align:center;box-sizing:border-box;line-height:100%%;text-decoration:none;display:inline-block;max-width:100%%;mso-padding-alt:0px;padding:12px 24px 12px 24px" target="_blank">
                                                <span style="max-width:100%%;display:inline-block;line-height:120%%;mso-padding-alt:0px;mso-text-raise:9px">Califícanos</span>
                                            </a>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);margin-bottom:24px;margin-top:16px">
                                Gracias por confiar en nosotros.
                            </p>
                            <p style="font-size:16px;line-height:24px;color:rgb(55,65,81);font-weight:700;margin-bottom:16px;margin-top:16px">
                                El equipo de U-nidos
                            </p>
                        </td>
                    </tr>
                </tbody>
            </table>
            <table align="center" width="100%%" border="0" cellpadding="0" cellspacing="0" role="presentation" style="margin-left:auto;margin-right:auto;margin-top:32px;max-width:600px">
                <tbody>
                    <tr style="width:100%%">
                        <td>
                            <p style="text-align:center;font-size:12px;color:rgb(107,114,128);margin:0px;line-height:24px">
                                © 2025 U-nidos S.A. Todos los derechos reservados.
                            </p>
                            <p style="text-align:center;font-size:12px;color:rgb(107,114,128);margin:0px;line-height:24px">
                                Bogotá, Colombia.
                            </p>
                        </td>
                    </tr>
                </tbody>
            </table>
        </body>
        </html>
        """, rateUrl);
    }

}
