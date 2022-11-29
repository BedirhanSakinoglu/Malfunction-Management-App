import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MalfunctionDataService } from '../service/malfunction-data.service';
import { DetailsComponent } from '../details/details.component';
import { gql, Apollo } from 'apollo-angular';
import { Malfunction } from '../models/malfunction';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { Detail } from '../models/details';

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
  selector: 'app-malfunctions',
  templateUrl: './malfunctions.component.html',
  styleUrls: ['./malfunctions.component.css']
})

export class MalfunctionsComponent implements OnInit {

  malfunctions: Malfunction[] = [];
  closeResult = '';

  stompClient: any;
  webSocketEndPoint: string = 'http://localhost:8085/ws';
  topic: string = "/topic/malfunction";

  updatePanelFlag: boolean = false;
  detailsPanelFlag: boolean = false;
  malfunction !: Malfunction;
  currentStage !: string;
  details !: Detail;
  updatedFlag: boolean = false;
  updateNotificationFlag: boolean = false;
  addNotificationFlag: boolean = false;
  updatedMalfunctionId: string = "";
  addedMalfunctionId: string = "";

  displayStartFlag: boolean = true;
  displayEndFlag: boolean = true;
  displayStageFlag: boolean = true;

  filtersSidePanelFlag: boolean = false;
  idFilter:string = "";
  startFilter:string = "";
  endFilter:string = "";
  stageFilter:string = "";
  unfilteredMalfunctions: Malfunction[] = [];

  sortByStartFlag: boolean = false;
  sortByEndFlag: boolean = false;

  constructor(
    private malfunctionService: MalfunctionDataService,
    private router:Router,
    private modalService: NgbModal,
    private apollo: Apollo,
  ) {}

  seeDetails(malfunction: Malfunction){
    var id = malfunction.malfunctionId;
    this.router.navigate(['details',id]);
  }


  updateMalfunction(malfunction: Malfunction){
    var id = malfunction.malfunctionId;
    this.router.navigate(['update',id]);
  }

  ngOnInit(): void {
    this.connect()

    this.apollo.watchQuery<any>({
      query: GetMalfunctionsQuery
    })
    .valueChanges
    .subscribe(({data, loading}) => {
      console.log(loading);
      this.malfunctions = Object.assign(this.malfunctions,data.allMalfunctions);
      //this.malfunctions = data.allMalfunctions;
      console.log(this.malfunctions)

      this.unfilteredMalfunctions = this.malfunctions;
    })
  }

  // Web Socket *******************************************************************************************************
  
  connect(): void {
    console.log('WebSocket Connection');
    const ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function(frame:any) {
        _this.stompClient.subscribe(_this.topic, function(sdkEvent:any) {
            _this.onMessageReceived(sdkEvent);
        });
    }, this.errorCallBack);
  }

  disconnect(): void {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log('Disconnected');
  }

   // on error, schedule a reconnection attempt
   errorCallBack(error:any) {
    console.log('errorCallBack -> ' + error);
    setTimeout(() => {
        this.connect();
    }, 5000);
}
  onMessageReceived(message: any) {
    //console.log(message.body)
    let malfunction: Malfunction = JSON.parse(message.body);
    //console.log('Message Received from Server :: ' + malfunction.malfunctionId);
    this.handleUpdate(malfunction);
  }

  handleUpdate(message: Malfunction){
    let itemIndex = this.malfunctions.findIndex(item => item.malfunctionId === message.malfunctionId);
    if(itemIndex == -1){
      this.malfunctions.unshift(message);
      this.addedMalfunctionId = message.malfunctionId;
      this.addNotificationFlag = true;
    }
    else{
      this.malfunctions[itemIndex] = message;
      this.updatedMalfunctionId = message.malfunctionId;
      this.updateNotificationFlag = true;
    }
    console.log(this.malfunctions[itemIndex].stage);

    this.unfilteredMalfunctions = this.malfunctions;
    if(this.sortByStartFlag || this.sortByEndFlag){
      this.applyFilter();
    }
  }

  //Update Side Panel **********************************************************************************
  openUpdateSidePanel(malfunction:Malfunction){
    this.updatePanelFlag = true;
    this.detailsPanelFlag = false;
    this.filtersSidePanelFlag = false;
    this.malfunction = malfunction;
    this.currentStage = malfunction.stage[0];
    this.details = malfunction.details;

  }

  closeUpdateSidePanel(){
    this.updatePanelFlag = false;
    this.updatedFlag = false;
  }

  saveMalfunction(){
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

    this.updatedFlag = true;
  }

  //Details Side Panel **********************************************************************************
  openDetailsSidePanel(malfunction:Malfunction){
    this.detailsPanelFlag = true;
    this.updatePanelFlag = false;
    this.filtersSidePanelFlag = false;
    this.malfunction = malfunction;
    this.currentStage = malfunction.stage[0];
    this.details = malfunction.details;

  }

  closeDetailsSidePanel(){
    this.detailsPanelFlag = false;
  }

  closeUpdateNotification(){
    this.updateNotificationFlag = false;
  }

  closeAddNotification(){
    this.addNotificationFlag = false;
  }

  displayStart(){
    this.displayStartFlag = !this.displayStartFlag
  }

  displayEnd(){
    this.displayEndFlag = !this.displayEndFlag
  }

  displayStage(){
    this.displayStageFlag = !this.displayStageFlag
  }

  openFiltersSidePanel(){
    this.filtersSidePanelFlag = true;
    this.updatePanelFlag = false;
    this.detailsPanelFlag = false;
  }

  closeFiltersSidePanel(){
    this.filtersSidePanelFlag = false;
  }

  applyFilter(){
    this.malfunctions = this.unfilteredMalfunctions;

    let filteredMalfunctions:Malfunction[] = [];
    for(let i = 0 ; i < this.malfunctions.length ; i++){
      if(this.malfunctions[i].malfunctionId.includes(this.idFilter)){
        if((this.malfunctions[i].startTimestamp.toString()).includes((this.startFilter.toString()))){
          if((this.malfunctions[i].endTimestamp.toString()).includes((this.endFilter.toString()))){
            if(this.malfunctions[i].stage[0].includes(this.stageFilter)){
              filteredMalfunctions.unshift(this.malfunctions[i]);
            }
          }
        }
      }
    }

    //this.malfunctions = filteredMalfunctions;

    if(this.sortByStartFlag){
      console.log("sorted by start")
      let sortedList = []
      var index = 0;
      for(var i = 0 ; i < filteredMalfunctions.length ; i++){
        var max = -1
        for(var t = i ; t < filteredMalfunctions.length ; t++){
          if(filteredMalfunctions[t].startTimestamp > max){
            max = filteredMalfunctions[t].startTimestamp
            index = t
          }
        }
        var temp = filteredMalfunctions[i];
        filteredMalfunctions[i] = filteredMalfunctions[index];
        filteredMalfunctions[index] = temp
      }
    }
    else if(this.sortByEndFlag){
      console.log("sorted by end")
      let sortedList = []
      var index = 0;
      for(var i = 0 ; i < filteredMalfunctions.length ; i++){
        var max = -1
        for(var t = i ; t < filteredMalfunctions.length ; t++){
          if(filteredMalfunctions[t].endTimestamp > max){
            max = filteredMalfunctions[t].endTimestamp
            index = t
          }
        }
        var temp = filteredMalfunctions[i];
        filteredMalfunctions[i] = filteredMalfunctions[index];
        filteredMalfunctions[index] = temp
      }
    }

    this.malfunctions = filteredMalfunctions;
    
  }

  removeAllFilters(){
    this.idFilter = ""
    this.startFilter = ""
    this.endFilter = ""
    this.stageFilter = ""
    this.malfunctions = this.unfilteredMalfunctions;
  }

  sortByStart(){
    this.sortByEndFlag = false;
    this.sortByStartFlag = true;
  }

  sortByEnd(){
    this.sortByEndFlag = true;
    this.sortByStartFlag = false;
  }
}


