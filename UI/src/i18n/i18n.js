import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

import {TRANSLATION_ES} from "./translations-es";
import {TRANSLATION_EN} from "./translations-en";

i18n
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        resources: {
            en: {
                translation: TRANSLATION_EN
            },
            es: {
                translation: TRANSLATION_ES
            }
        }
    });

const changeLanguage = async (lang) => {
    await i18n.changeLanguage(lang);
}