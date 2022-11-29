import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Apollo, gql } from 'apollo-angular';
import { Detail } from '../models/details';
import { Malfunction } from '../models/malfunction';
import { MalfunctionDataService } from '../service/malfunction-data.service';

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

const UpdateMalfunctionQuery = gql`
mutation ($malfunctionId:String!, $stage:String!, $crew_id:String!, $description:String!, $malfunction_cause:String!){
  updateMalfunction(malfunctionId:$malfunctionId, stage:$stage, crew_id:$crew_id, description:$description, malfunction_cause:$malfunction_cause) {
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
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css']
})
export class UpdateComponent implements OnInit {

  malfunctionId!:string;
  startTimestamp!:number;
  details!:Detail;
  malfunction!: Malfunction;
  malfunctions: Malfunction[] = [];
  stage: string[] = [];
  currentStage: string = "";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private malfunctionService: MalfunctionDataService,
    private apollo: Apollo
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
      this.currentStage = this.malfunction.stage[0];

      this.malfunction = Object.assign({},this.malfunction);
      this.details = Object.assign({},this.malfunction.details);
      this.stage = Object.assign(this.malfunction.stage);
    })
  }

  saveMalfunction(){

    //this.stage = Object.assign([], this.malfunction.stage);
    //this.stage.unshift(this.currentStage);
    //if(this.malfunction.stage[0] === this.currentStage){
    //}
    //else{ 
    //}
    console.log("malfunctionId: ", this.malfunction.malfunctionId)
    this.apollo.mutate({
      mutation: UpdateMalfunctionQuery,
      variables: {
        malfunctionId: this.malfunction.malfunctionId,
        stage: this.currentStage,
        crew_id: this.details.crew_id,
        description: this.details.description,
        malfunction_cause: this.details.malfunction_cause,
      }
    }).subscribe(({ data }) => {
      console.log("Updated")
      this.router.navigate(['malfunctions'])
    });
  }

  cancel(){
    this.router.navigate(['malfunctions'])
  }

}
