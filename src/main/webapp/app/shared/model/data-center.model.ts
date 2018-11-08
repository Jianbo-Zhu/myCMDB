import { IContactor } from 'app/shared/model//contactor.model';

export interface IDataCenter {
  id?: number;
  dcName?: string;
  address?: string;
  contractor?: IContactor;
}

export const defaultValue: Readonly<IDataCenter> = {};
