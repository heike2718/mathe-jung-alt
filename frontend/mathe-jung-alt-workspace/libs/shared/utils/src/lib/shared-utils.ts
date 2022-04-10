export interface MjaEntity {
    id: string | number;
};

export interface MjaEntityWrapper<T extends MjaEntity> {
    id: string | number;
    entity: T
};

/** Stellt Mengenoperationen auf MjaEntities zur Verfügung, die z.B. in ngrx-Selektoren hilfreich sein können */
export class MjaSetUtils<T extends MjaEntity> {

    /** die gegebenen elemente werden nur dann berücksichtigt, wenn sie eine property id : string | number haben */
    public toMjaEntityArray(array: T[]): MjaEntityWrapper<T>[]{

        const result: MjaEntityWrapper<T>[] = [];
        array.forEach(
            (element: T) => {
                result.push({id: element.id, entity: element});
            }
        )
        return result;
    }

    public getDifferenzmenge(alle: MjaEntityWrapper<T>[], auszuschliessen: MjaEntityWrapper<T>[]): T[] {

        const result: T[] = [];
        const map: Map<number | string, MjaEntityWrapper<T>> = new Map();

        auszuschliessen.forEach(
            (element: MjaEntityWrapper<T>) => map.set(element.id, element)
        );

        alle.forEach(element => {
            if (!map.has(element.id)) {
                result.push(element.entity);
            }
        });

        return result;
    }
};
