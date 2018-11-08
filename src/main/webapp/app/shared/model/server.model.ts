import { Moment } from 'moment';
import { IComponentEntity } from 'app/shared/model//component-entity.model';
import { IDataCenter } from 'app/shared/model//data-center.model';
import { IContactor } from 'app/shared/model//contactor.model';

export interface IServer {
  id?: number;
  hostname?: string;
  ipAddress?: string;
  macAddress?: string;
  position?: string;
  brand?: string;
  memSize?: number;
  coreNo?: number;
  osVersion?: string;
  vendor?: string;
  purchaseDate?: Moment;
  warrantyDate?: Moment;
  components?: IComponentEntity[];
  dataCenter?: IDataCenter;
  owner?: IContactor;
  vendorContact?: IContactor;
}

export const defaultValue: Readonly<IServer> = {};
