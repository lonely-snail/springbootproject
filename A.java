//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dianju.signatureServer.cache;

import com.dianju.core.Util;
import com.dianju.modules.adhibition.models.AdhDao;
import com.dianju.modules.cert.models.Cert;
import com.dianju.modules.cert.models.CertDao;
import com.dianju.modules.seal.models.Seal;
import com.dianju.modules.seal.models.SealDao;
import com.dianju.modules.templateAIP.models.SealRule;
import com.dianju.modules.templateAIP.models.SealRuleDao;
import com.dianju.modules.templateAIP.models.TemplateAIP;
import com.dianju.modules.templateAIP.models.TemplateAIPDao;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.net.util.Base64;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Cache {
    private static HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    private static ServletContext servletContext;
    public static String certPath;

    public Cache() {
    }

    private static Map cacheInit(String cacheName) {
        Object o = servletContext.getAttribute(cacheName);
        if (o == null) {
            Map m = new HashMap();
            servletContext.setAttribute(cacheName, m);
            o = m;
        }

        Map m = (Map)o;
        return m;
    }

    private static void cacheDestroy(String cacheName, String key) {
        Map m = cacheInit(cacheName);
        m.remove(key);
    }

    public static Map getAdhibitionBySysNo(String sysNo) {
        Map<String, Map> adhibitionBySysNo = cacheInit("AdhibitionBySysNo");
        Map<String, String> adhibitionIdToSysNo = cacheInit("AdhibitionIdToSysNo");
        if (!adhibitionBySysNo.containsKey(sysNo)) {
            AdhDao adhDao = (AdhDao)Util.getBean("adhDao");

            try {
                List<Object[]> adhs = adhDao.getAdhInfoBySysNo(sysNo);
                if (adhs.size() == 0) {
                    return null;
                } else {
                    Map adh = new HashMap();
                    adh.put("id", ((Object[])adhs.get(0))[0].toString());
                    adh.put("ip", ((Object[])adhs.get(0))[1].toString());
                    adh.put("password", ((Object[])adhs.get(0))[2].toString());
                    adh.put("sealRuleNumber", adhDao.getSealRuleNumberBySysNo(sysNo));
                    adh.put("templateAIPName", adhDao.getTemplateAIPNameBySysNo(sysNo));
                    adhibitionBySysNo.put(sysNo, adh);
                    adhibitionIdToSysNo.put(adh.get("id").toString(), sysNo);
                    return adh;
                }
            } catch (Exception var6) {
                return null;
            }
        } else {
            return (Map)adhibitionBySysNo.get(sysNo);
        }
    }

    public static void cacheDestroyAdhibitionById(String id) {
        Map<String, Map> adhibitionBySysNo = cacheInit("AdhibitionBySysNo");
        Map<String, String> adhibitionIdToSysNo = cacheInit("AdhibitionIdToSysNo");
        adhibitionBySysNo.remove(adhibitionIdToSysNo.get(id));
        adhibitionIdToSysNo.remove(id);
    }

    public static void cacheDestroyTemplateAipById(String templateAIPId) {
        Map cacheTemplateAipById = cacheInit("TemplateAipById");
        Map cacheTemplateAipByName = cacheInit("TemplateAipByName");
        TemplateAIP templateAIP = (TemplateAIP)cacheTemplateAipById.get(templateAIPId);
        if (templateAIP != null) {
            cacheTemplateAipByName.remove(templateAIP.getName());
        }

        cacheTemplateAipById.remove(templateAIPId);
    }

    public static TemplateAIP getTemplateAipByName(String templateName) {
        Map<String, TemplateAIP> cacheTemplateAipByName = cacheInit("TemplateAipByName");
        Map<String, TemplateAIP> cacheTemplateAipById = cacheInit("TemplateAipById");
        if (!cacheTemplateAipByName.containsKey(templateName)) {
            TemplateAIPDao templateAIPDao = (TemplateAIPDao)Util.getBean("templateAIPDao");

            try {
                TemplateAIP templateAIP = templateAIPDao.findOneByName(templateName);
                if (templateAIP != null) {
                    cacheTemplateAipByName.put(templateName, templateAIP);
                    cacheTemplateAipById.put(templateAIP.getId(), templateAIP);
                }

                return templateAIP;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (TemplateAIP)cacheTemplateAipByName.get(templateName);
        }
    }

    public static void cacheDestroyCertById(String certId) {
        Map m = cacheInit("CertById");
        Map m1 = cacheInit("CertByName");
        Cert cert = (Cert)m.get("certId");
        m.remove(certId);
        if (cert != null) {
            certFileDelete(cert);
            m1.remove(cert.getName());
        }

    }

    public static Cert getCertById(String certId) {
        Map<String, Cert> cacheCert = cacheInit("CertById");
        Map<String, Cert> cacheCertByName = cacheInit("CertByName");
        if (!cacheCert.containsKey(certId)) {
            CertDao certDao = (CertDao)Util.getBean("certDao");

            try {
                Cert cert = (Cert)certDao.findOne(certId);
                if (cert != null) {
                    certToFile(cert);
                    cacheCert.put(certId, cert);
                    cacheCertByName.put(cert.getName(), cert);
                }

                return cert;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (Cert)cacheCert.get(certId);
        }
    }

    public static Cert getCertByName(String certName) {
        Map<String, Cert> cacheCert = cacheInit("CertById");
        Map<String, Cert> cacheCertByName = cacheInit("CertByName");
        if (!cacheCertByName.containsKey(certName)) {
            CertDao certDao = (CertDao)Util.getBean("certDao");

            try {
                Cert cert = certDao.findOneByName(certName);
                if (cert != null) {
                    certToFile(cert);
                    cacheCert.put(cert.getId(), cert);
                    cacheCertByName.put(certName, cert);
                }

                return cert;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (Cert)cacheCertByName.get(certName);
        }
    }

    public static SealRule getSealRuleByNumber(String SealRuleNumber) {
        Map<String, SealRule> cacheSealRuleByNumber = cacheInit("SealRuleByNumber");
        Map<String, SealRule> cacheSealRuleById = cacheInit("SealRuleById");
        if (!cacheSealRuleByNumber.containsKey(SealRuleNumber)) {
            SealRuleDao sealRuleDao = (SealRuleDao)Util.getBean("sealRuleDao");

            try {
                SealRule sealRule = sealRuleDao.findOneByNumber(SealRuleNumber);
                if (sealRule != null) {
                    cacheSealRuleByNumber.put(SealRuleNumber, sealRule);
                    cacheSealRuleById.put(sealRule.getId(), sealRule);
                }

                return sealRule;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (SealRule)cacheSealRuleByNumber.get(SealRuleNumber);
        }
    }

    public static void cacheDestroySealRuleById(String sealRuleId) {
        Map cacheSealRuleByNumber = cacheInit("SealRuleByNumber");
        Map cacheSealRuleById = cacheInit("SealRuleById");
        SealRule sealRule = (SealRule)cacheSealRuleById.get(sealRuleId);
        if (sealRule != null) {
            cacheSealRuleByNumber.remove(sealRule.getNumber());
        }

        cacheSealRuleById.remove(sealRuleId);
    }

    public static Seal getSealById(String SealId) {
        Map<String, Seal> cacheSealById = cacheInit("SealById");
        Map<String, Seal> cacheSealByName = cacheInit("SealByName");
        if (!cacheSealById.containsKey(SealId)) {
            SealDao sealRuleDao = (SealDao)Util.getBean("sealDao");

            try {
                Seal seal = sealRuleDao.findOneById(SealId);
                if (seal != null) {
                    cacheSealByName.put(seal.getName(), seal);
                    cacheSealById.put(SealId, seal);
                }

                return seal;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (Seal)cacheSealById.get(SealId);
        }
    }

    public static Seal getSealByName(String sealName) {
        Map<String, Seal> cacheSealById = cacheInit("SealById");
        Map<String, Seal> cacheSealByName = cacheInit("SealByName");
        if (!cacheSealByName.containsKey(sealName)) {
            SealDao sealRuleDao = (SealDao)Util.getBean("sealDao");

            try {
                Seal seal = sealRuleDao.findOneByName(sealName);
                if (seal != null) {
                    cacheSealByName.put(sealName, seal);
                    cacheSealById.put(seal.getId(), seal);
                }

                return seal;
            } catch (Exception var5) {
                return null;
            }
        } else {
            return (Seal)cacheSealByName.get(sealName);
        }
    }

    public static void cacheDestroySealById(String sealId) {
        Map m = cacheInit("SealById");
        Map m1 = cacheInit("SealByName");
        Seal seal = (Seal)m.get(sealId);
        if (seal != null) {
            m1.remove(seal.getName());
        }

        m.remove(sealId);
    }

    public static void certToFile(Cert cert) {
        String suffix;
        if ("0".equals(cert.getType())) {
            suffix = "pfx";
        } else {
            suffix = "cer";
        }

        File f = new File(certPath + "/" + cert.getId() + "." + suffix);
        if (!f.isFile()) {
            BufferedOutputStream outStream = null;

            try {
                outStream = new BufferedOutputStream(new FileOutputStream(f));
                String str = cert.getPfxContent().substring(cert.getPfxContent().indexOf(",") + 1);
                outStream.write(Base64.decodeBase64(str));
                outStream.flush();
            } catch (Exception var13) {
                var13.printStackTrace();
            } finally {
                try {
                    outStream.close();
                } catch (Exception var12) {
                    ;
                }

            }
        }

    }

    public static void certFileDelete(Cert cert) {
        String suffix;
        if ("0".equals(cert.getType())) {
            suffix = "pfx";
        } else {
            suffix = "cer";
        }

        File f = new File(certPath + "/" + cert.getId() + "." + suffix);
        f.delete();
    }

    static {
        servletContext = request.getServletContext();
        certPath = Util.getSystemDictionary("upload_path") + "/cache/cert/";
    }
}
