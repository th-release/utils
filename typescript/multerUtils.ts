import Multer from 'multer';
import path from 'path';
import getRandom from './getRandom';
import { Request } from 'express';

interface MulterConfig {
  callbackPath?: string,
  cbFunction?: (
    req: Request,
    file: Express.Multer.File,
    callback:
      (error: Error, destination: string) => void
  ) => void
  fnFunction?: (
    req: Request,
    file: Express.Multer.File,
    callback: (error: Error, destination: string) => void
  ) => void,
  filter?: (
    req: Request,
    file: Express.Multer.File,
    callback: Multer.FileFilterCallback
  ) => void
}

type MimeType =
  | 'video/mp4' | 'image/gif' | 'text/plain' | 'text/html'
  | 'text/css' | 'text/javascript' | 'audio/midi' | 'audio/mpeg'
  | 'audio/webm' | 'audio/ogg' | 'audio/wav' | 'video/webm'
  | 'video/ogg' | 'audio/aac' | 'application/x-abiword' | 'application/x-freearc'
  | 'image/avif' | 'video/x-msvideo' | 'application/vnd.amazon.ebook' | 'application/octet-stream'
  | 'image/bmp' | 'application/x-bzip' | 'application/x-bzip2' | 'application/x-cdf' | 'application/x-csh'
  | 'text/css' | 'text/csv' | 'application/msword' | 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
  | 'application/vnd.ms-fontobject' | 'application/epub+zip' | 'application/gzip' | 'image/vnd.microsoft.icon'
  | 'text/calendar' | 'application/java-archive' | 'image/jpeg' | 'application/json'
  | 'application/ld+json' | 'audio/x-midi' | 'video/mpeg' | 'application/vnd.apple.installer+xml'
  | 'application/vnd.oasis.opendocument.presentation' | 'application/vnd.oasis.opendocument.spreadsheet'
  | 'application/vnd.oasis.opendocument.text' | 'application/ogg' | 'audio/opus' | 'font/otf'
  | 'image/png' | 'application/pdf' | 'application/x-httpd-php' | 'application/vnd.ms-powerpoint'
  | 'application/vnd.openxmlformats-officedocument.presentationml.presentation' | 'application/vnd.rar'
  | 'application/rtf' | 'application/x-sh' | 'image/svg+xml' | 'application/x-tar' | 'image/tiff'
  | 'video/mp2t' | 'font/ttf' | 'application/vnd.visio' | 'image/webp' | 'font/woff' | 'font/woff2'
  | 'application/xhtml+xml' | 'application/vnd.ms-excel' | 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  | 'application/vnd.mozilla.xul+xml' | 'application/zip' | 'video/3gpp' | 'audio/3gpp' | 'video/3gpp2' | 'audio/3gpp2'
  | 'application/x-7z-compressed'

export default class MulterUtils {
  private multer: Multer.Multer;
  private storage: Multer.StorageEngine;

  constructor(config: MulterConfig, mime: MimeType[], fileSize: number) {
    this.storage = Multer.diskStorage({
      destination: (req, file, callback) => {
        if (config.cbFunction && !config.callbackPath) {
          return config.cbFunction(req, file, callback)
        } else {
          return callback(null, path.resolve(process.cwd(), 'public/' + config.callbackPath))
        }
      },
      filename: (req, file, callback) => {
        if (config.fnFunction) {
          return config.fnFunction(req, file, callback);
        } else {
          const now = new Date();
          const extension = path.extname(file.originalname);
          return callback(null, file.fieldname + '-' + getRandom('all', 32) + now.getTime() + extension);
        }
      }
    })

    this.multer = Multer({
      storage: this.storage,
      fileFilter: (req, file, callback) => {
        if (config.filter) {
          return config.filter(req, file, callback);
        } else {
          if (!mime.includes(file.mimetype as MimeType))
            return callback(null, false);
          return callback(null, true);
        }
      },
      limits: { fileSize: fileSize * 1024 * 1024 },
    })
  }

  getMulter() {
    return this.multer;
  }
}
