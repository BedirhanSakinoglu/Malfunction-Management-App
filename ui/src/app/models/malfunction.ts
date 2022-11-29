import { Detail } from "./details";

export interface Malfunction {
    malfunctionId : string,
    startTimestamp : number,
    endTimestamp : number,
    stage : string [],
    details: Detail,
}