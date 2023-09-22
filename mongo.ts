import mongoose, { CallbackWithoutResultAndOptionalError, IndexDefinition, Schema } from "mongoose";

interface MongoConsfig {
  logging?: boolean
}

export default class Mongo {
  private logging: boolean

  // public Model
  constructor(MongoConstructor?: MongoConsfig) {
    if (MongoConstructor && MongoConstructor.logging)
      this.logging = true
    else this.logging = false
    this.connect();
  }

  private async connect() {
    await mongoose.connect(`mongodb://${process.env.DATABASE_HOST}:${process.env.DATABASE_PORT}/${process.env.DATABASE_USERNAME}`)
      .then((res) => {
        res.connection.on('error', (err) => {
          console.log('[Mongoose] Database Load Error:' + err + '\x1b[0m')
        })

        res.connection.once('open', () => {
          console.log('[Mongoose] Database Load. \x1b[0m')
        })
      })
      .catch((err) => {
        console.log('[Mongoose] Database Load Error:' + err + '\x1b[0m')
        process.exit(1)
      })
  }

  public releaseModel(name: string, Schema: any) {
    const schema = new mongoose.Schema(Schema);
    const model = mongoose.model(name, schema)
    return model
  }

  async create<T extends Document>(model: mongoose.Model<T>, data: any): Promise<T> {
    try {
      const createdData = await model.create(data)
      if (this.logging)
        console.log(createdData)
      return createdData;
    } catch (error) {
      if (this.logging)
        console.log(error)
      throw error;
    }
  }

  async findOne<T extends Document>(model: mongoose.Model<T>, query: any): Promise<T | null> {
    try {
      const data = await model.findOne(query);
      return data;
    } catch (error) {
      throw error;
    }
  }

  async findById<T extends Document>(model: mongoose.Model<T>, id: mongoose.ObjectId): Promise<T | null> {
    try {
      const data = await model.findById(id);
      return data;
    } catch (error) {
      throw error;
    }
  }

  async update<T extends Document>(model: mongoose.Model<T>, id: mongoose.ObjectId, data: any): Promise<T | null> {
    try {
      const updatedData = await model.findByIdAndUpdate(id, data, { new: true });
      if (this.logging)
        console.log(updatedData)
      return updatedData;
    } catch (error) {
      if (this.logging)
        console.log(error)
      throw error;
    }
  }

  async delete<T extends Document>(model: mongoose.Model<T>, id: mongoose.ObjectId): Promise<void> {
    await model.findByIdAndDelete(id)
      .then((res) => {
        if (this.logging)
          console.log(res)
      })
      .catch((err) => {
        if (this.logging)
          console.log(err)
        throw err
      })
  }
}
