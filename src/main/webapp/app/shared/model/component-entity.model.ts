import { IVersion } from 'app/shared/model//version.model';
import { IApplication } from 'app/shared/model//application.model';
import { IServer } from 'app/shared/model//server.model';

export const enum ComponentType {
  MODULE = 'MODULE',
  MIDDLEWARE = 'MIDDLEWARE'
}

export interface IComponentEntity {
  id?: number;
  comName?: string;
  comType?: ComponentType;
  versions?: IVersion[];
  app?: IApplication;
  server?: IServer;
}

export const defaultValue: Readonly<IComponentEntity> = {};
