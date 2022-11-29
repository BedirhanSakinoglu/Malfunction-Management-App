import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Malfunction } from '../models/malfunction';
import { Detail } from '../models/details';
import { MalfunctionDataService } from '../service/malfunction-data.service';
import { Apollo, gql } from 'apollo-angular';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

const GetMalfunctionsQuery = gql`
query {
  allMalfunctions
  {
  malfunctionId
  startTimestamp
  endTimestamp
  stage
  details
    {
    detailId
    crew_id
    description
    malfunction_cause
    }
  }
}
`;

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})

export class DetailsComponent implements OnInit {

  malfunctionId!:string;
  startTimestamp!:number;
  details!:Detail;
  malfunction!:Malfunction;
  malfunctions:Malfunction[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private malfunctionService: MalfunctionDataService,
    private modalService: NgbModal,
    private apollo: Apollo,
  ) { }

  ngOnInit(): void {
    this.malfunctionId = this.route.snapshot.params['id'];
    //console.log(this.malfunctionId);

    this.apollo.watchQuery<any>({
      query: GetMalfunctionsQuery
    })
    .valueChanges
    .subscribe(({data, loading}) => {
      console.log(loading);
      this.malfunctions = data.allMalfunctions;
      let filteredElements=this.malfunctions.filter(malfunction => malfunction.malfunctionId === this.malfunctionId);
      this.malfunction = filteredElements[0];
      this.details = this.malfunction.details;
    })

  }

}
