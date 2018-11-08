import { IComponentEntity } from 'app/shared/model//component-entity.model';

export interface IApplication {
  id?: number;
  appName?: string;
  environment?: string;
  components?: IComponentEntity[];
}

export const defaultValue: Readonly<IApplication> = {};
