import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { Observable } from "rxjs";
import { QuellenFacade } from "../application/quellen.facade";
import { Quelle } from "../entities/quelle";


export class QuellenDataSource implements DataSource<Quelle> {

    constructor(private quellenFacade: QuellenFacade){ }
    
    
    connect(collectionViewer: CollectionViewer): Observable<readonly Quelle[]> {
        throw new Error("Method not implemented.");
    }
    
    disconnect(collectionViewer: CollectionViewer): void {
        throw new Error("Method not implemented.");
    }




}