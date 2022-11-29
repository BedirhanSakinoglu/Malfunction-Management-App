import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Malfunction } from '../models/malfunction';
import { Detail } from '../models/details';

@Injectable({
  providedIn: 'root'
})
export class MalfunctionDataService {

  url = "http://localhost:8087";

  constructor(
    private http:HttpClient
  ) { }

  retrieveAllMalfunctions(){
    return this.http.get<Malfunction[]>(`${this.url}/malfunctions`);
  }

  retrieveDetails(malfunctionId: string, startTimestamp: number){
    return this.http.get<Detail>(`${this.url}/malfunctions/details/${malfunctionId}/${startTimestamp}`);
  }

  retrieveMalfunctionsById(malfunctionId: string){
    return this.http.get<Malfunction[]>(`${this.url}/malfunctions/${malfunctionId}`);
  }

  retrieveMalfunction(malfunctionId: string, startTimestamp: number){
    return this.http.get<Malfunction>(`${this.url}/malfunctions/${malfunctionId}/${startTimestamp}`);
  }

  updateMalfunction(malfunctionId:string, malfunction:Malfunction){
    return this.http.put(`${this.url}/malfunctions/${malfunctionId}`, malfunction);
  }

  updateDetail(detailsId:number, details:Detail){
    return this.http.put(`${this.url}/details/${detailsId}`, details);
  }
}
